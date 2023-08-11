package com.diac.ydeas.ideas.repository;

import com.diac.ydeas.domain.enumeration.IdeaStatus;
import com.diac.ydeas.domain.model.IdeaReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для хранения объектов IdeaReview (Рассмотрение идеи)
 */
@Repository
public interface IdeaReviewRepository extends JpaRepository<IdeaReview, Integer> {

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
}