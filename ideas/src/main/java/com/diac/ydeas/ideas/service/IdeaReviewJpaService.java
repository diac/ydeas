package com.diac.ydeas.ideas.service;

import com.diac.ydeas.domain.dto.IdeaReviewNotificationDto;
import com.diac.ydeas.domain.enumeration.IdeaStatus;
import com.diac.ydeas.domain.exception.ResourceConstraintViolationException;
import com.diac.ydeas.domain.exception.ResourceNotFoundException;
import com.diac.ydeas.domain.model.Idea;
import com.diac.ydeas.domain.model.IdeaReview;
import com.diac.ydeas.ideas.repository.IdeaReviewRepository;
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
 * Сервис для работы с объектами IdeaReview
 */
@Service
@AllArgsConstructor
public class IdeaReviewJpaService implements IdeaReviewService {

    /**
     * Шаблон сообщения о том, что рассмотрение идеи не существует
     */
    private static final String IDEA_REVIEW_DOES_NOT_EXIST_MESSAGE = "Idea review #%s does not exist";

    /**
     * Тема Kafka для обмена сообщениями о рассмотрении идей
     */
    private static final String IDEA_REVIEW_KAFKA_NOTIFICATION_TOPIC = "idea-review-notification";

    /**
     * Шаблон Kafka-продюсера для отправки объектов IdeaReviewNotificationDto
     */
    private final KafkaTemplate<Integer, IdeaReviewNotificationDto> kafkaTemplate;

    /**
     * Сервис для работы с объектами Idea
     */
    private final IdeaService ideaService;

    /**
     * Репозиторий для хранения объектов IdeaReview
     */
    private final IdeaReviewRepository ideaReviewRepository;

    /**
     * Найти все рассмотрения идей
     *
     * @return Список с рассмотрениями идей
     */
    @Override
    public List<IdeaReview> findAll() {
        return ideaReviewRepository.findAll();
    }

    /**
     * Получить страницу с рассмотрениями идей
     *
     * @param pageRequest Объект PageRequest
     * @return Страница с рассмотрениями идей
     */
    @Override
    public Page<IdeaReview> getPage(PageRequest pageRequest) {
        return ideaReviewRepository.findAll(pageRequest);
    }

    /**
     * Найти все рассмотрения по ID пользователя
     *
     * @param reviewerUserUuid UUID пользователя-эксперта
     * @return Список с рассмотрениями идеи
     */
    @Override
    public List<IdeaReview> findAllByReviewerUserUuid(UUID reviewerUserUuid) {
        return ideaReviewRepository.findAllByReviewerUserUuid(reviewerUserUuid);
    }

    /**
     * Найти все рассмотрения по статусу идеи
     *
     * @param ideaStatus Статус идеи
     * @return Список с рассмотрениями идеи
     */
    @Override
    public List<IdeaReview> findAllByIdeaStatus(IdeaStatus ideaStatus) {
        return ideaReviewRepository.findAllByIdeaStatus(ideaStatus);
    }

    /**
     * Найти рассмотрение идеи по ID идеи
     *
     * @param ideaId Идентификатор идеи
     * @return Рассмотрение идеи
     * @throws ResourceNotFoundException Если ничего не найдено
     */
    @Override
    public IdeaReview findByIdeaId(int ideaId) {
        return ideaReviewRepository.findById(ideaId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(String.format(IDEA_REVIEW_DOES_NOT_EXIST_MESSAGE, ideaId))
                );
    }

    /**
     * Добавить новое рассмотрение идеи в систему
     *
     * @param ideaReview Новое рассмотрение идеи
     * @return Сохраненное рассмотрение идеи
     * @throws ResourceConstraintViolationException в случае, если при обращении к ресурсу нарушаются наложенные на него ограничения
     */
    @Override
    public IdeaReview add(IdeaReview ideaReview) {
        try {
            return ideaReviewRepository.save(ideaReview);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new ResourceConstraintViolationException(e.getMessage());
        }
    }

    /**
     * Обновить данные рассмотрения идеи в системе
     *
     * @param ideaId     Идентификатор идеи
     * @param ideaReview Объект с обновленными данными рассмотрения идеи
     * @return Обновленное рассмотрение идеи
     * @throws ResourceNotFoundException            При попытке обновить несуществующее рассмотрение идеи
     * @throws ResourceConstraintViolationException в случае, если при обращении к ресурсу нарушаются наложенные на него ограничения
     */
    @Override
    public IdeaReview update(int ideaId, IdeaReview ideaReview) {
        try {
            return ideaReviewRepository.findById(ideaId)
                    .map(ideaReviewInDb -> {
                        ideaReview.setIdea(
                                Idea.builder().id(ideaId)
                                        .build()
                        );
                        return ideaReviewRepository.save(ideaReview);
                    }).orElseThrow(
                            () -> new ResourceNotFoundException(
                                    String.format(IDEA_REVIEW_DOES_NOT_EXIST_MESSAGE, ideaId)
                            )
                    );
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new ResourceConstraintViolationException(e.getMessage());
        }
    }

    /**
     * Удалить рассмотрение идеи из системы
     *
     * @param ideaId Идентификатор идеи
     */
    @Override
    public void delete(int ideaId) {
        ideaReviewRepository.findById(ideaId)
                .ifPresentOrElse(
                        ideaReview -> ideaReviewRepository.deleteById(ideaId),
                        () -> {
                            throw new ResourceNotFoundException(
                                    String.format(IDEA_REVIEW_DOES_NOT_EXIST_MESSAGE, ideaId)
                            );
                        }
                );
    }

    /**
     * Одобрить идею
     *
     * @param ideaId           Идентификатор идеи
     * @param reviewerUserUuid UUID пользователя-эксперта
     */
    @Override
    public void approve(int ideaId, UUID reviewerUserUuid) {
        Idea idea = ideaService.findById(ideaId);
        IdeaReview ideaReview = IdeaReview.builder()
                .ideaId(idea.getId())
                .idea(idea)
                .reviewerUserUuid(reviewerUserUuid)
                .ideaStatus(IdeaStatus.APPROVED)
                .build();
        try {
            ideaReviewRepository.save(ideaReview);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new ResourceConstraintViolationException(e.getMessage());
        }
        kafkaTemplate.send(
                IDEA_REVIEW_KAFKA_NOTIFICATION_TOPIC,
                new IdeaReviewNotificationDto(
                        idea.getId(),
                        idea.getTitle(),
                        idea.getAuthorUuid(),
                        ideaReview.getIdeaStatus()
                )
        );
    }

    /**
     * Отклонить идею
     *
     * @param ideaId           Идентификатор идеи
     * @param reviewerUserUuid UUID пользователя-эксперта
     */
    @Override
    public void decline(int ideaId, UUID reviewerUserUuid) {
        Idea idea = ideaService.findById(ideaId);
        IdeaReview ideaReview = IdeaReview.builder()
                .ideaId(ideaId)
                .idea(idea)
                .reviewerUserUuid(reviewerUserUuid)
                .ideaStatus(IdeaStatus.DECLINED)
                .build();
        try {
            ideaReviewRepository.save(ideaReview);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new ResourceConstraintViolationException(e.getMessage());
        }
        kafkaTemplate.send(
                IDEA_REVIEW_KAFKA_NOTIFICATION_TOPIC,
                new IdeaReviewNotificationDto(
                        idea.getId(),
                        idea.getTitle(),
                        idea.getAuthorUuid(),
                        ideaReview.getIdeaStatus()
                )
        );
    }
}