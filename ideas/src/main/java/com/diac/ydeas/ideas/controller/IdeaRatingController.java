package com.diac.ydeas.ideas.controller;

import com.diac.ydeas.domain.model.IdeaRating;
import com.diac.ydeas.ideas.service.IdeaRatingService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер, реализующий доступ к объектам модели IdeaRating
 */
@RestController
@AllArgsConstructor
@RequestMapping("/idea_rating")
public class IdeaRatingController {

    /**
     * Сервис для работы с объектами IdeaRating
     */
    private final IdeaRatingService ideaRatingService;

    /**
     * Получить рейтинговый список идей
     *
     * @param pageable Объект Pageable
     * @return Тело ответа с рейтинговым списком идей и статусом OK
     */
    @GetMapping("")
    public ResponseEntity<Page<IdeaRating>> rating(Pageable pageable) {
        return new ResponseEntity<>(
                ideaRatingService.rating(pageable),
                HttpStatus.OK
        );
    }
}