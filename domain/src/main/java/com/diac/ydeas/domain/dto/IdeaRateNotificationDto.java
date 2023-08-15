package com.diac.ydeas.domain.dto;

import com.diac.ydeas.domain.enumeration.Rate;

import java.util.UUID;

/**
 * DTO для передачи данных об оценке идеи
 *
 * @param ideaId       Идентификатор идеи
 * @param ideaTitle    Заголовок идеи
 * @param ideaAuthorId UUID автора идеи
 * @param rate         Оценка идеи
 */
public record IdeaRateNotificationDto(
        int ideaId,
        String ideaTitle,
        UUID ideaAuthorId,
        Rate rate
) {
}