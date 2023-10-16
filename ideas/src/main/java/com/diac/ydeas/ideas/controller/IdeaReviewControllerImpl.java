package com.diac.ydeas.ideas.controller;

import com.diac.ydeas.ideas.service.IdeaReviewService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

/**
 * Контроллер, реализующий доступ к объектам модели IdeaReview
 */
@RestController
@AllArgsConstructor
@RequestMapping("/idea_review")
public class IdeaReviewControllerImpl implements IdeaReviewController {

    /**
     * Сервис для работы с объектами IdeaReview
     */
    private final IdeaReviewService ideaReviewService;

    /**
     * Одобрить идею
     *
     * @param ideaId    Идентификатор идеи
     * @param principal Объект Principal
     */
    @Override
    public void approve(
            @PathVariable("idea_id") int ideaId,
            Principal principal
    ) {
        ideaReviewService.approve(ideaId, UUID.fromString(principal.getName()));
    }

    /**
     * Отклонить идею
     *
     * @param ideaId    Идентификатор идеи
     * @param principal Объект Principal
     */
    @Override
    public void decline(
            @PathVariable("idea_id") int ideaId,
            Principal principal
    ) {
        ideaReviewService.decline(ideaId, UUID.fromString(principal.getName()));
    }
}