package com.diac.ydeas.domain.dto;

/**
 * DTO для определения связи между идеей и медиа-объектом
 *
 * @param ideaId        Идентификатор идеи
 * @param mediaObjectId Идентификатор медиа-объекта
 */
public record IdeaMediaObjectAssociationDto(int ideaId, int mediaObjectId) {
}