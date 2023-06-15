package com.diac.ydeas.ideas.repository;

import com.diac.ydeas.domain.model.Idea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для хранения объектов Idea (Идея)
 */
@Repository
public interface IdeaRepository extends JpaRepository<Idea, Integer> {
}