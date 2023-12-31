package com.diac.ydeas.ideas.repository;

import com.diac.ydeas.domain.model.IdeaRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для хранения объектов IdeaRating (Рейтинг идей)
 */
@Repository
public interface IdeaRatingRepository extends JpaRepository<IdeaRating, Integer> {

    Page<IdeaRating> findAllByOrderByRatingDesc(Pageable pageable);
}