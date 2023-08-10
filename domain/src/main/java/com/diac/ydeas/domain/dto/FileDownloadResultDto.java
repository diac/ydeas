package com.diac.ydeas.domain.dto;

import org.springframework.core.io.Resource;

/**
 * DTO с результатом скачивания файла
 *
 * @param resource Ресурс с содержимым файла
 * @param mediaType Тип медиа файла
 */
public record FileDownloadResultDto(Resource resource, String mediaType) {
}