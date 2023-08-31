package com.diac.ydeas.ideas.repository;

import com.diac.ydeas.domain.model.Idea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Репозиторий для хранения объектов Idea (Идея)
 */
@Repository
public interface IdeaRepository extends JpaRepository<Idea, Integer> {

    /**
     * Получить страницу с идеями по UUID автора идей
     *
     * @param authorUuid UUID автора идей
     * @param pageable   Объект Pageable
     * @return Страница с идеями
     */
    Page<Idea> findByAuthorUuid(UUID authorUuid, Pageable pageable);
}