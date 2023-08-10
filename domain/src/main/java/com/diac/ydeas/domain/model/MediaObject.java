package com.diac.ydeas.domain.model;

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
public class MediaObject {

    /**
     * Идентификатор медиа-объекта
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private int id;

    /**
     * URL медиа-объекта
     */
    @NotNull(message = "URL is required")
    @NotBlank(message = "URL cannot be blank")
    private String url;

    /**
     * Имя файла медиа-объекта
     */
    @Column(name = "file_name")
    @NotNull(message = "Filename is required")
    @NotBlank(message = "Filename cannot be blank")
    private String fileName;

    /**
     * Оригинальное имя файла медиа-объекта
     */
    @Column(name = "original_file_name")
    @NotNull(message = "Original filename is required")
    @NotBlank(message = "Original filename cannot be blank")
    private String originalFileName;

    /**
     * Тип медиа медиа-объекта
     */
    @Column(name = "media_type")
    @NotNull(message = "Media Type is required")
    @NotBlank(message = "Media Type cannot be blank")
    private String mediaType;

    /**
     * Длина контента медиа-объекта
     */
    @Column(name = "content_length")
    @NotNull(message = "Content Length is required")
    private Long contentLength;

    /**
     * Заголовок медиа-объекта
     */
    @NotNull(message = "Title is required")
    @NotBlank(message = "Title cannot be blank")
    private String title;

    /**
     * Описание медиа-объекта
     */
    @NotNull(message = "Description is required")
    @NotBlank(message = "Description cannot be blank")
    private String description;
}