package com.diac.ydeas.domain.dto;

import com.diac.ydeas.domain.model.MediaObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * DTO для ввода идей в систему
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "DTO для ввода идей в систему")
public class IdeaInputDto {

    /**
     * Заголовок идеи
     */
    @NotNull(message = "Idea title is required")
    @NotBlank(message = "Idea title cannot be blank")
    @Schema(description = "Заголовок идеи", example = "My Idea")
    private String title;

    /**
     * Описание идеи
     */
    @NotNull(message = "Idea description is required")
    @NotBlank(message = "Idea description cannot be blank")
    @Schema(description = "Описание идеи", example = "Lorem ipsum")
    private String description;

    /**
     * Прикрепленные медиа-объекты идеи
     */
    private Set<MediaObject> attachments = new HashSet<>();
}