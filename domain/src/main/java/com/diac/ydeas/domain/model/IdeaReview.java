package com.diac.ydeas.domain.model;

import com.diac.ydeas.domain.enumeration.IdeaStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

/**
 * Модель данных "Рассмотрение идеи"
 */
@Entity
@Table(name = "idea_review")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Schema(description = "Модель данных \"Рассмотрение идеи\"")
public class IdeaReview {

    /**
     * Идентификатор идеи
     */
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "idea_id")
    @Schema(description = "Идентификатор идеи", example = "1")
    private int ideaId;

    /**
     * Идея
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "idea_id")
    @MapsId
    @JsonBackReference
    private Idea idea;

    /**
     * UUID пользователя-эксперта, рассмотревшего идею
     */
    @NotNull(message = "Idea reviewer UUID is required")
    @Column(name = "reviewer_user_uuid")
    @Schema(description = "UUID пользователя-эксперта, рассмотревшего идею", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID reviewerUserUuid;

    /**
     * Статус идеи после рассмотрения
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "idea_status")
    @NotNull(message = "Idea status is required")
    @Schema(description = "Статус идеи после рассмотрения", example = "APPROVED")
    private IdeaStatus ideaStatus;
}