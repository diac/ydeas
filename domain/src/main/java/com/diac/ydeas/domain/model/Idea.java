package com.diac.ydeas.domain.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Модель данных "Идея"
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = false)
@Builder
public class Idea {

    /**
     * Идентификатор идеи
     */
    @EqualsAndHashCode.Include
    private int id;

    /**
     * Заголовок идеи
     */
    @NotNull(message = "Idea title is required")
    @NotBlank(message = "Idea title cannot be blank")
    private String title;

    /**
     * Описание идеи
     */
    @NotNull(message = "Idea description is required")
    @NotBlank(message = "Idea description cannot be blank")
    private String description;

    /**
     * Автор идеи
     */
    @NotNull(message = "Idea author is required")
    private User author;
}