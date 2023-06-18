package com.diac.ydeas.ideas.service;

import com.diac.ydeas.domain.model.IdeaRating;

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
}