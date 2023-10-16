package com.diac.ydeas.ideas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

/**
 * Контроллер, реализующий доступ к объектам модели IdeaRate
 */
@Tag(name = "IdeaRateController", description = "Контроллер, реализующий доступ к объектам модели IdeaRate")
public interface IdeaRateController {

    /**
     * Поставить идее оценку "Нравится"
     *
     * @param ideaId    Идентификатор идеи
     * @param principal Объект Principal
     */
    @PostMapping("/{idea_id}/like")
    @Operation(summary = "Поставить идее оценку \"Нравится\"")
    void like(
            @Parameter(description = "Идентификатор идеи") int ideaId,
            @Parameter(description = "Объект Principal") Principal principal
    );

    /**
     * Поставить идее оценку "Не нравится"
     *
     * @param ideaId    Идентификатор идеи
     * @param principal Объект Principal
     */
    @PostMapping("/{idea_id}/dislike")
    @Operation(summary = "Поставить идее оценку \"Не нравится\"")
    void dislike(
            @Parameter(description = "Идентификатор идеи") int ideaId,
            @Parameter(description = "Объект Principal") Principal principal
    );
}