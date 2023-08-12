package com.diac.ydeas.ideas.repository;

import com.diac.ydeas.domain.model.IdeaRate;
import com.diac.ydeas.domain.model.IdeaRateId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Репозиторий для хранения объектов IdeaRate (Оценка идеи)
 */
@Repository
public interface IdeaRateRepository extends JpaRepository<IdeaRate, IdeaRateId> {

    /**
     * Найти все оценки для идеи по ID идеи
     *
     * @param ideaId Идентификатор идеи
     * @return Список с оценками
     */
    List<IdeaRate> findAllByIdeaRateIdIdeaId(int ideaId);

    /**
     * Найти все оценки по UUID пользователя
     *
     * @param userUuid UUID пользователя
     * @return Список с оценками
     */
    List<IdeaRate> findAllByIdeaRateIdUserUuid(UUID userUuid);
}