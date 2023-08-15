package com.diac.ydeas.ideas.controller;

import com.diac.ydeas.ideas.service.IdeaRateService;
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
     * @param ideaId    Идентификатор идеи
     * @param principal Объект Principal
     * @return Тело ответа со статусом OK
     */
    @PostMapping("/{idea_id}/like")
    public ResponseEntity<Void> like(
            @PathVariable("idea_id") int ideaId,
            Principal principal
    ) {
        ideaRateService.like(ideaId, UUID.fromString(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Поставить идее оценку "Не нравится"
     *
     * @param ideaId    Идентификатор идеи
     * @param principal Объект Principal
     * @return Тело ответа со статусом OK
     */
    @PostMapping("/{idea_id}/dislike")
    public ResponseEntity<Void> dislike(
            @PathVariable("idea_id") int ideaId,
            Principal principal
    ) {
        ideaRateService.dislike(ideaId, UUID.fromString(principal.getName()));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}