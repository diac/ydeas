package com.diac.ydeas.domain.dto;

/**
 * DTO с деталями хранимого объекта
 *
 * @param bucketName Имя бакета
 * @param objectKey  Ключ объекта
 * @param mediaType Тип медиа
 * @param contentLength Длина контента объекта
 */
public record StoredObjectDetailsDto(
        String bucketName,
        String objectKey,
        String mediaType,
        long contentLength
) {

}