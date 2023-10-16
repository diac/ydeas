package com.diac.ydeas.domain.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Модель данных \"Идея\"")
public class Idea {

    /**
     * Идентификатор идеи
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Schema(description = "Идентификатор идеи", example = "1")
    private int id;

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
    @Schema(description = "Заголовок идеи", example = "Lorem ipsum dolor sit amet")
    private String description;

    /**
     * UUID автора идеи
     */
    @NotNull(message = "Idea author UUID is required")
    @Column(name = "author_uuid")
    @Schema(description = "UUID автора идеи", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID authorUuid;

    /**
     * Дата и время создания идеи
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Оценки идеи
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "ideaRateId.idea")
    @JsonManagedReference
    private Set<IdeaRate> ideaRates;

    /**
     * Результат рассмотрения идеи
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "idea")
    @JsonManagedReference
    private IdeaReview ideaReview;

    @ManyToMany
    @JoinTable(
            name = "idea_attachment",
            joinColumns = {@JoinColumn(name = "idea_id")},
            inverseJoinColumns = {@JoinColumn(name = "media_object_id")}
    )
    private Set<MediaObject> attachments = new HashSet<>();
}