package com.diac.ydeas.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO для определения связи между идеей и медиа-объектом
 *
 * @param ideaId        Идентификатор идеи
 * @param mediaObjectId Идентификатор медиа-объекта
 */
@Schema(description = "DTO для определения связи между идеей и медиа-объектом")
public record IdeaMediaObjectAssociationDto(
        @Schema(description = "Идентификатор идеи", example = "1") int ideaId,
        @Schema(description = "Идентификатор медиа-объекта", example = "1") int mediaObjectId
) {
}