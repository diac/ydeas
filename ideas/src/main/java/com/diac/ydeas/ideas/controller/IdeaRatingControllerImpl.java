package com.diac.ydeas.ideas.controller;

import com.diac.ydeas.domain.model.IdeaRating;
import com.diac.ydeas.ideas.service.IdeaRatingService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер, реализующий доступ к объектам модели IdeaRating
 */
@RestController
@AllArgsConstructor
@RequestMapping("/idea_rating")
public class IdeaRatingControllerImpl implements IdeaRatingController {

    /**
     * Сервис для работы с объектами IdeaRating
     */
    private final IdeaRatingService ideaRatingService;

    /**
     * Получить рейтинговый список идей
     *
     * @param pageable Объект Pageable
     * @return Рейтинговый список идей
     */
    @Override
    public Page<IdeaRating> rating(@PageableDefault Pageable pageable) {
        return ideaRatingService.rating(pageable);
    }
}