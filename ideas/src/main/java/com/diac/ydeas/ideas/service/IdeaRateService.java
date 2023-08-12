package com.diac.ydeas.ideas.service;

import com.diac.ydeas.domain.model.IdeaRate;
import com.diac.ydeas.domain.model.IdeaRateId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы с объектами IdeaRate
 */
public interface IdeaRateService {

    /**
     * Найти все оценки идей
     *
     * @return Список с оценками идей
     */
    List<IdeaRate> findAll();

    /**
     * Получить страницу с оценками идей
     *
     * @param pageRequest Объект PageRequest
     * @return Страница с оценками идей
     */
    Page<IdeaRate> getPage(PageRequest pageRequest);

    /**
     * Найти все оценки для идеи по ID идеи
     *
     * @param ideaId Идентификатор идеи
     * @return Список с оценками
     */
    List<IdeaRate> findAllByIdeaId(int ideaId);

    /**
     * Найти все оценки по ID пользователя
     *
     * @param userUuid UUID пользователя
     * @return Список с оценками
     */
    List<IdeaRate> findAllByUserUuid(UUID userUuid);

    /**
     * Найти оценку идеи по ID
     *
     * @param ideaRateId Идентификатор оценки идеи (объект IdeaRateId)
     * @return Оценка идеи
     */
    IdeaRate findById(IdeaRateId ideaRateId);

    /**
     * Добавить новую оценку идеи в систему
     *
     * @param ideaRate Новая оценка идеи
     * @return Сохраненная оценка идеи
     */
    IdeaRate add(IdeaRate ideaRate);

    /**
     * Обновить данные Оценки идеи в системе
     *
     * @param ideaRateId Идентификатор оценки идеи (объект IdeaRateId)
     * @param ideaRate   Объект с обновленными данными оценки идеи
     * @return Обновленная оценка идеи
     */
    IdeaRate update(IdeaRateId ideaRateId, IdeaRate ideaRate);

    /**
     * Удалить оценку идеи из системы
     *
     * @param ideaRateId Идентификатор оценки идеи (объект IdeaRateId)
     */
    void delete(IdeaRateId ideaRateId);
}