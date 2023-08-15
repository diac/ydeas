package com.diac.ydeas.ideas.service;

import com.diac.ydeas.domain.dto.IdeaRateNotificationDto;
import com.diac.ydeas.domain.enumeration.Rate;
import com.diac.ydeas.domain.exception.ResourceConstraintViolationException;
import com.diac.ydeas.domain.exception.ResourceNotFoundException;
import com.diac.ydeas.domain.model.Idea;
import com.diac.ydeas.domain.model.IdeaRate;
import com.diac.ydeas.domain.model.IdeaRateId;
import com.diac.ydeas.ideas.repository.IdeaRateRepository;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы с объектами IdeaRate
 */
@Service
@AllArgsConstructor
public class IdeaRateJpaService implements IdeaRateService {

    /**
     * Шаблон сообщения о том, что оценка идеи не существует
     */
    private static final String IDEA_RATE_DOES_NOT_EXIST_MESSAGE = "Idea rate #%s does not exist";

    /**
     * Тема Kafka для обмена сообщениями об оценках идей
     */
    private static final String IDEA_RATE_KAFKA_NOTIFICATION_TOPIC = "idea-rate-notification";

    /**
     * Шаблон Kafka-продюсера для отправки объектов IdeaRateNotificationDto
     */
    private final KafkaTemplate<Integer, IdeaRateNotificationDto> kafkaTemplate;

    /**
     * Репозиторий для хранения объектов IdeaRate
     */
    private final IdeaRateRepository ideaRateRepository;

    /**
     * Сервис для работы с объектами Idea
     */
    private final IdeaService ideaService;

    /**
     * Найти все оценки идей
     *
     * @return Список с оценками идей
     */
    @Override
    public List<IdeaRate> findAll() {
        return ideaRateRepository.findAll();
    }

    /**
     * Получить страницу с оценками идей
     *
     * @param pageRequest Объект PageRequest
     * @return Страница с оценками идей
     */
    @Override
    public Page<IdeaRate> getPage(PageRequest pageRequest) {
        return ideaRateRepository.findAll(pageRequest);
    }

    /**
     * Найти все оценки для идеи по ID идеи
     *
     * @param ideaId Идентификатор идеи
     * @return Список с оценками
     */
    @Override
    public List<IdeaRate> findAllByIdeaId(int ideaId) {
        return ideaRateRepository.findAllByIdeaRateIdIdeaId(ideaId);
    }

    /**
     * Найти все оценки по ID пользователя
     *
     * @param userUuid UUID пользователя
     * @return Список с оценками
     */
    @Override
    public List<IdeaRate> findAllByUserUuid(UUID userUuid) {
        return ideaRateRepository.findAllByIdeaRateIdUserUuid(userUuid);
    }

    /**
     * Найти оценку идеи по ID
     *
     * @param ideaRateId Идентификатор оценки идеи (объект IdeaRateId)
     * @return Оценка идеи
     * @throws ResourceNotFoundException Если ничего не найдено
     */
    @Override
    public IdeaRate findById(IdeaRateId ideaRateId) {
        return ideaRateRepository.findById(ideaRateId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(String.format(IDEA_RATE_DOES_NOT_EXIST_MESSAGE, ideaRateId))
                );
    }

    /**
     * Добавить новую оценку идеи в систему
     *
     * @param ideaRate Новая оценка идеи
     * @return Сохраненная оценка идеи
     * @throws ResourceConstraintViolationException в случае, если при обращении к ресурсу нарушаются наложенные на него ограничения
     */
    @Override
    public IdeaRate add(IdeaRate ideaRate) {
        try {
            return ideaRateRepository.save(ideaRate);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new ResourceConstraintViolationException(e.getMessage());
        }
    }

    /**
     * Обновить данные Оценки идеи в системе
     *
     * @param ideaRateId Идентификатор оценки идеи (объект IdeaRateId)
     * @param ideaRate   Объект с обновленными данными оценки идеи
     * @return Обновленная оценка идеи
     * @throws ResourceNotFoundException            При попытке обновить несуществующую оценку идеи
     * @throws ResourceConstraintViolationException в случае, если при обращении к ресурсу нарушаются наложенные на него ограничения
     */
    @Override
    public IdeaRate update(IdeaRateId ideaRateId, IdeaRate ideaRate) {
        try {
            return ideaRateRepository.findById(ideaRateId)
                    .map(ideaRateInDb -> {
                        ideaRate.setIdeaRateId(ideaRateId);
                        return ideaRateRepository.save(ideaRate);
                    }).orElseThrow(
                            () -> new ResourceNotFoundException(String.format(IDEA_RATE_DOES_NOT_EXIST_MESSAGE, ideaRateId))
                    );
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new ResourceConstraintViolationException(e.getMessage());
        }
    }

    /**
     * Удалить оценку идеи из системы
     *
     * @param ideaRateId Идентификатор оценки идеи (объект IdeaRateId)
     * @throws ResourceNotFoundException При попытке удалить несуществующую оценку идеи
     */
    @Override
    public void delete(IdeaRateId ideaRateId) {
        ideaRateRepository.findById(ideaRateId)
                .ifPresentOrElse(
                        ideaRate -> ideaRateRepository.deleteById(ideaRateId),
                        () -> {
                            throw new ResourceNotFoundException(
                                    String.format(IDEA_RATE_DOES_NOT_EXIST_MESSAGE, ideaRateId)
                            );
                        }
                );
    }

    /**
     * Поставить оценку "Нравится"
     *
     * @param ideaId   Идентификатор идеи
     * @param userUuid UUID пользователя, поставившего оценку
     */
    @Override
    public void like(int ideaId, UUID userUuid) {
        Idea idea = ideaService.findById(ideaId);
        IdeaRateId ideaRateId = IdeaRateId.builder()
                .idea(idea)
                .userUuid(userUuid)
                .build();
        IdeaRate ideaRate = IdeaRate.builder()
                .ideaRateId(ideaRateId)
                .rate(Rate.LIKE)
                .build();
        try {
            ideaRateRepository.save(ideaRate);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new ResourceConstraintViolationException(e.getMessage());
        }
        kafkaTemplate.send(
                IDEA_RATE_KAFKA_NOTIFICATION_TOPIC,
                new IdeaRateNotificationDto(
                        idea.getId(),
                        idea.getTitle(),
                        idea.getAuthorUuid(),
                        ideaRate.getRate()
                )
        );
    }

    /**
     * Поставить оценку "Не нравится"
     *
     * @param ideaId   Идентификатор идеи
     * @param userUuid UUID пользователя, поставившего оценку
     */
    @Override
    public void dislike(int ideaId, UUID userUuid) {
        Idea idea = ideaService.findById(ideaId);
        IdeaRateId ideaRateId = IdeaRateId.builder()
                .idea(idea)
                .userUuid(userUuid)
                .build();
        IdeaRate ideaRate = IdeaRate.builder()
                .ideaRateId(ideaRateId)
                .rate(Rate.DISLIKE)
                .build();
        try {
            ideaRateRepository.save(ideaRate);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new ResourceConstraintViolationException(e.getMessage());
        }
        kafkaTemplate.send(
                IDEA_RATE_KAFKA_NOTIFICATION_TOPIC,
                new IdeaRateNotificationDto(
                        idea.getId(),
                        idea.getTitle(),
                        idea.getAuthorUuid(),
                        ideaRate.getRate()
                )
        );
    }
}