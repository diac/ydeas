package com.diac.ydeas.ideas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

/**
 * Контроллер, реализующий доступ к объектам модели IdeaReview
 */
@Tag(name = "IdeaReviewController", description = "Контроллер, реализующий доступ к объектам модели IdeaReview")
public interface IdeaReviewController {

    /**
     * Одобрить идею
     *
     * @param ideaId    Идентификатор идеи
     * @param principal Объект Principal
     */
    @PostMapping("/{idea_id}/approve")
    @Operation(summary = "Одобрить идею")
    void approve(
            @Parameter(description = "Идентификатор идеи") int ideaId,
            @Parameter(description = "Объект Principal") Principal principal
    );

    /**
     * Отклонить идею
     *
     * @param ideaId    Идентификатор идеи
     * @param principal Объект Principal
     */
    @PostMapping("/{idea_id}/decline")
    @Operation(summary = "Отклонить идею")
    void decline(
            @Parameter(description = "Идентификатор идеи") int ideaId,
            @Parameter(description = "Объект Principal") Principal principal
    );
}