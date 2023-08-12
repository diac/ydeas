package com.diac.ydeas.ideas.service;

import com.diac.ydeas.domain.exception.ResourceConstraintViolationException;
import com.diac.ydeas.domain.exception.ResourceNotFoundException;
import com.diac.ydeas.domain.model.IdeaRate;
import com.diac.ydeas.domain.model.IdeaRateId;
import com.diac.ydeas.ideas.repository.IdeaRateRepository;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
     * Репозиторий для хранения объектов IdeaRate
     */
    private final IdeaRateRepository ideaRateRepository;

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
}