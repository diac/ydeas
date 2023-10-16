package com.diac.ydeas.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Модель данных "Медиа-объект"
 */
@Entity
@Table(name = "media_object")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Schema(description = "Модель данных \"Медиа-объект\"")
public class MediaObject {

    /**
     * Идентификатор медиа-объекта
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Schema(description = "Идентификатор медиа-объекта", example = "1")
    private int id;

    /**
     * URL медиа-объекта
     */
    @NotNull(message = "URL is required")
    @NotBlank(message = "URL cannot be blank")
    @Schema(description = "URL медиа-объекта", example = "https://somesite.com/somepicture.jpg")
    private String url;

    /**
     * Имя файла медиа-объекта
     */
    @Column(name = "file_name")
    @NotNull(message = "Filename is required")
    @NotBlank(message = "Filename cannot be blank")
    @Schema(description = "Имя файла медиа-объекта", example = "somepicture.jpg")
    private String fileName;

    /**
     * Оригинальное имя файла медиа-объекта
     */
    @Column(name = "original_file_name")
    @NotNull(message = "Original filename is required")
    @NotBlank(message = "Original filename cannot be blank")
    @Schema(description = "Оригинальное имя файла медиа-объекта", example = "somepicture.jpg")
    private String originalFileName;

    /**
     * Тип медиа медиа-объекта
     */
    @Column(name = "media_type")
    @NotNull(message = "Media Type is required")
    @NotBlank(message = "Media Type cannot be blank")
    @Schema(description = "Тип медиа медиа-объекта", example = "image/jpeg")
    private String mediaType;

    /**
     * Длина контента медиа-объекта
     */
    @Column(name = "content_length")
    @NotNull(message = "Content Length is required")
    @Schema(description = "Длина контента медиа-объекта", example = "1234")
    private Long contentLength;

    /**
     * Заголовок медиа-объекта
     */
    @NotNull(message = "Title is required")
    @NotBlank(message = "Title cannot be blank")
    @Schema(description = "Заголовок медиа-объекта", example = "My picture")
    private String title;

    /**
     * Описание медиа-объекта
     */
    @NotNull(message = "Description is required")
    @NotBlank(message = "Description cannot be blank")
    @Schema(description = "Описание медиа-объекта", example = "Lorem ipsum")
    private String description;
}