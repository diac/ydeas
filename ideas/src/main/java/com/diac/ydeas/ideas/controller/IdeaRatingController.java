package com.diac.ydeas.ideas.controller;

import com.diac.ydeas.domain.model.IdeaRating;
import com.diac.ydeas.ideas.service.IdeaRatingService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер, реализующий доступ к объектам модели IdeaRating
 */
@RestController
@AllArgsConstructor
@RequestMapping("/idea_rating")
public class IdeaRatingController {

    private static final int RATINGS_PER_PAGE = 20;

    /**
     * Сервис для работы с объектами IdeaRating
     */
    private final IdeaRatingService ideaRatingService;

    /**
     * Получить рейтинговый список идей
     *
     * @return Тело ответа с рейтинговым списком идей и статусом OK
     */
    @GetMapping("")
    public ResponseEntity<Page<IdeaRating>> rating(
            @RequestParam(name = "page", required = false, defaultValue = "1") int pageNumber
    ) {
        return new ResponseEntity<>(
                ideaRatingService.rating(PageRequest.of(pageNumber - 1, RATINGS_PER_PAGE)),
                HttpStatus.OK
        );
    }
}