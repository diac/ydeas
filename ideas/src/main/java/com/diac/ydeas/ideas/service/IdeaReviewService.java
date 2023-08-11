package com.diac.ydeas.ideas.service;

import com.diac.ydeas.domain.enumeration.IdeaStatus;
import com.diac.ydeas.domain.model.IdeaReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы с объектами IdeaReview
 */
public interface IdeaReviewService {

    /**
     * Найти все рассмотрения идей
     *
     * @return Список с рассмотрениями идей
     */
    List<IdeaReview> findAll();

    /**
     * Получить страницу с рассмотрениями идей
     *
     * @param pageRequest Объект PageRequest
     * @return Страница с рассмотрениями идей
     */
    Page<IdeaReview> getPage(PageRequest pageRequest);

    /**
     * Найти все рассмотрения по ID пользователя
     *
     * @param reviewerUserUuid UUID пользователя-эксперта
     * @return Список с рассмотрениями идеи
     */
    List<IdeaReview> findAllByReviewerUserUuid(UUID reviewerUserUuid);

    /**
     * Найти все рассмотрения по статусу идеи
     *
     * @param ideaStatus Статус идеи
     * @return Список с рассмотрениями идеи
     */
    List<IdeaReview> findAllByIdeaStatus(IdeaStatus ideaStatus);

    /**
     * Найти рассмотрение идеи по ID идеи
     *
     * @param ideaId Идентификатор идеи
     * @return Рассмотрение идеи
     */
    IdeaReview findByIdeaId(int ideaId);

    /**
     * Добавить новое рассмотрение идеи в систему
     *
     * @param ideaReview Новое рассмотрение идеи
     * @return Сохраненное рассмотрение идеи
     */
    IdeaReview add(IdeaReview ideaReview);

    /**
     * Обновить данные рассмотрения идеи в системе
     *
     * @param ideaId     Идентификатор идеи
     * @param ideaReview Объект с обновленными данными рассмотрения идеи
     * @return Обновленное рассмотрение идеи
     */
    IdeaReview update(int ideaId, IdeaReview ideaReview);

    /**
     * Удалить рассмотрение идеи из системы
     *
     * @param ideaId Идентификатор идеи
     */
    void delete(int ideaId);

    /**
     * Одобрить идею
     *
     * @param ideaId   Идентификатор идеи
     * @param expertId UUID пользователя-эксперта
     */
    void approve(int ideaId, UUID expertId);

    /**
     * Отклонить идею
     *
     * @param ideaId   Идентификатор идеи
     * @param expertId UUID пользователя-эксперта
     */
    void decline(int ideaId, UUID expertId);
}