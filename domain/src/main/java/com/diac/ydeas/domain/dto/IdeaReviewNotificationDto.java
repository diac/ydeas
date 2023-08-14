package com.diac.ydeas.domain.dto;

import com.diac.ydeas.domain.enumeration.IdeaStatus;

import java.util.UUID;

/**
 * DTO для передачи данных о рассмотрении идеи
 *
 * @param ideaId         Идентификатор идеи
 * @param ideaTitle      Заголовок идеи
 * @param ideaAuthorUuid UUID автора идеи
 * @param ideaStatus     Статус идеи после рассмотрения
 */
public record IdeaReviewNotificationDto(
        int ideaId,
        String ideaTitle,
        UUID ideaAuthorUuid,
        IdeaStatus ideaStatus
) {
}