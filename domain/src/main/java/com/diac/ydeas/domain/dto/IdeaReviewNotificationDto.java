package com.diac.ydeas.domain.dto;

import com.diac.ydeas.domain.enumeration.IdeaStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * DTO для передачи данных о рассмотрении идеи
 *
 * @param ideaId         Идентификатор идеи
 * @param ideaTitle      Заголовок идеи
 * @param ideaAuthorUuid UUID автора идеи
 * @param ideaStatus     Статус идеи после рассмотрения
 */
@Schema(description = "DTO для передачи данных о рассмотрении идеи")
public record IdeaReviewNotificationDto(
        @Schema(description = "Идентификатор идеи", example = "1") int ideaId,
        @Schema(description = "Заголовок идеи", example = "My Idea") String ideaTitle,
        @Schema(description = "UUID автора идеи", example = "550e8400-e29b-41d4-a716-446655440000") UUID ideaAuthorUuid,
        @Schema(description = "Статус идеи после рассмотрения", example = "APPROVED") IdeaStatus ideaStatus
) {
}