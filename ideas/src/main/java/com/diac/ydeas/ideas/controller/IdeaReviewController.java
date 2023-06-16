package com.diac.ydeas.ideas.controller;

import com.diac.ydeas.domain.enumeration.IdeaStatus;
import com.diac.ydeas.domain.model.Idea;
import com.diac.ydeas.domain.model.IdeaReview;
import com.diac.ydeas.ideas.service.IdeaReviewService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * @param ideaId         Идентификатор идеи
     * @param reviewerUserId Идентификатор пользователя-эксперта, одобрившего идею
     * @return Тело ответа со статусом OK
     */
    @PostMapping("/approve")
    public ResponseEntity<Void> approve(
            @RequestParam("idea_id") int ideaId,
            @RequestParam("reviewer_user_id") int reviewerUserId
    ) {
        IdeaReview ideaReview = IdeaReview.builder()
                .idea(
                        Idea.builder()
                                .id(ideaId)
                                .build()
                )
                .reviewerUserId(reviewerUserId)
                .ideaStatus(IdeaStatus.APPROVED)
                .build();
        ideaReviewService.add(ideaReview);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Отклонить идею
     *
     * @param ideaId         Идентификатор идеи
     * @param reviewerUserId Идентификатор пользователя-эксперта, отклонившего идею
     * @return Тело ответа со статусом OK
     */
    @PostMapping("/decline")
    public ResponseEntity<Void> decline(
            @RequestParam("idea_id") int ideaId,
            @RequestParam("reviewer_user_id") int reviewerUserId
    ) {
        IdeaReview ideaReview = IdeaReview.builder()
                .idea(
                        Idea.builder()
                                .id(ideaId)
                                .build()
                )
                .reviewerUserId(reviewerUserId)
                .ideaStatus(IdeaStatus.DECLINED)
                .build();
        ideaReviewService.add(ideaReview);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}