package com.diac.ydeas.ideas.controller;

import com.diac.ydeas.ideas.service.IdeaReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
public class IdeaReviewController {

    /**
     * Сервис для работы с объектами IdeaReview
     */
    private final IdeaReviewService ideaReviewService;

    /**
     * Одобрить идею
     *
     * @param ideaId    Идентификатор идеи
     * @param principal Объект Principal
     * @return Тело ответа со статусом OK
     */
    @PostMapping("/{idea_id}/approve")
    public ResponseEntity<Void> approve(
            @PathVariable("idea_id") int ideaId,
            Principal principal
    ) {
        ideaReviewService.approve(ideaId, UUID.fromString(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Отклонить идею
     *
     * @param ideaId    Идентификатор идеи
     * @param principal Объект Principal
     * @return Тело ответа со статусом OK
     */
    @PostMapping("/{idea_id}/decline")
    public ResponseEntity<Void> decline(
            @PathVariable("idea_id") int ideaId,
            Principal principal
    ) {
        ideaReviewService.decline(ideaId, UUID.fromString(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}