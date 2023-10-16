package com.diac.ydeas.ideas.controller;

import com.diac.ydeas.domain.model.IdeaRating;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер, реализующий доступ к объектам модели IdeaRating
 */
@Tag(name = "IdeaRatingController", description = "Контроллер, реализующий доступ к объектам модели IdeaRating")
public interface IdeaRatingController {

    /**
     * Получить рейтинговый список идей
     *
     * @param pageable Объект Pageable
     * @return Рейтинговый список идей
     */
    @GetMapping("")
    @Operation(summary = "Получить рейтинговый список идей")
    Page<IdeaRating> rating(@Parameter(description = "Объект Pageable") Pageable pageable);
}