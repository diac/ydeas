package com.diac.ydeas.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Модель данных "Идея"
 */
@Entity
@Table(name = "idea")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Idea {

    /**
     * Идентификатор идеи
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @NotNull(message = "Idea author UUID is required")
    @Column(name = "author_uuid")
    private UUID authorUuid;

    /**
     * Дата и время создания идеи
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(
            name = "idea_attachment",
            joinColumns = {@JoinColumn(name = "idea_id")},
            inverseJoinColumns = {@JoinColumn(name = "media_object_id")}
    )
    private Set<MediaObject> attachments = new HashSet<>();
}