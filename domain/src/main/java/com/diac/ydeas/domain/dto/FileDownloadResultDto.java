package com.diac.ydeas.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.core.io.Resource;

/**
 * DTO с результатом скачивания файла
 *
 * @param resource Ресурс с содержимым файла
 * @param mediaType Тип медиа файла
 */
@Schema(description = "DTO с результатом скачивания файла")
public record FileDownloadResultDto(
        @Schema(description = "Ресурс с содержимым файла") Resource resource,
        @Schema(description = "Тип медиа файла") String mediaType
) {
}