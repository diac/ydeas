package com.diac.ydeas.ideas.controller;

import com.diac.ydeas.domain.enumeration.Rate;
import com.diac.ydeas.domain.model.Idea;
import com.diac.ydeas.domain.model.IdeaRate;
import com.diac.ydeas.domain.model.IdeaRateId;
import com.diac.ydeas.ideas.service.IdeaRateService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер, реализующий доступ к объектам модели IdeaRate
 */
@RestController
@AllArgsConstructor
@RequestMapping("/idea_rate")
public class IdeaRateController {

    /**
     * Сервис для работы с объектами IdeaRate
     */
    private final IdeaRateService ideaRateService;

    /**
     * Поставить идее оценку "Нравится"
     *
     * @param ideaId Идентификатор идеи
     * @param userId Идентификатор пользователя, поставившего оценку
     * @return Тело ответа со статусом OK
     */
    @PostMapping("/like")
    public ResponseEntity<Void> like(
            @RequestParam("idea_id") int ideaId,
            @RequestParam("user_id") int userId
    ) {
        IdeaRateId ideaRateId = IdeaRateId.builder()
                .idea(
                        Idea.builder()
                                .id(ideaId)
                                .build()
                )
                .userId(userId)
                .build();
        ideaRateService.add(
                IdeaRate.builder()
                        .ideaRateId(ideaRateId)
                        .rate(Rate.LIKE)
                        .build()
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Поставить идее оценку "Не нравится"
     *
     * @param ideaId Идентификатор идеи
     * @param userId Идентификатор пользователя, поставившего оценку
     * @return Тело ответа со статусом OK
     */
    @PostMapping("/dislike")
    public ResponseEntity<Void> dislike(
            @RequestParam("idea_id") int ideaId,
            @RequestParam("user_id") int userId
    ) {
        IdeaRateId ideaRateId = IdeaRateId.builder()
                .idea(
                        Idea.builder()
                                .id(ideaId)
                                .build()
                )
                .userId(userId)
                .build();
        ideaRateService.add(
                IdeaRate.builder()
                        .ideaRateId(ideaRateId)
                        .rate(Rate.DISLIKE)
                        .build()
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }
}