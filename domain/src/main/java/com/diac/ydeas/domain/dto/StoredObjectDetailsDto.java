package com.diac.ydeas.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO с деталями хранимого объекта
 *
 * @param bucketName Имя бакета
 * @param objectKey  Ключ объекта
 * @param mediaType Тип медиа
 * @param contentLength Длина контента объекта
 */
@Schema(description = "DTO с деталями хранимого объекта")
public record StoredObjectDetailsDto(
        @Schema(description = "Имя бакета", example = "my-bucket") String bucketName,
        @Schema(description = "Ключ объекта", example = "object-key") String objectKey,
        @Schema(description = "Тип медиа", example = "image/jpeg") String mediaType,
        @Schema(description = "Длина контента объекта", example = "1234") long contentLength
) {

}