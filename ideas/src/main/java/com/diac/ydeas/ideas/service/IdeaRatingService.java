package com.diac.ydeas.ideas.service;

import com.diac.ydeas.domain.model.IdeaRating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Сервис для работы с объектами IdeaRating
 */
public interface IdeaRatingService {

    /**
     * Получить рейтинговый список идей
     *
     * @return Рейтинговый список идей
     */
    List<IdeaRating> rating();

    /**
     * Получить страницу рейтингового списка идей
     *
     * @param pageable Объект Pageable
     * @return Страница с идеями
     */
    Page<IdeaRating> rating(Pageable pageable);
}