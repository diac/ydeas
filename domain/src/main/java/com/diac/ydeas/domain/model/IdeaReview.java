package com.diac.ydeas.domain.model;

import com.diac.ydeas.domain.enumeration.IdeaStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class IdeaReview {

    /**
     * Идентификатор идеи
     */
    @Id
    @EqualsAndHashCode.Include
    @Column(name = "idea_id")
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
     * Пользователь-эксперт, рассмотревший идею
     */
    @NotNull(message = "Idea reviewer UUID is required")
    @Column(name = "reviewer_user_uuid")
    private UUID reviewerUserUuid;

    /**
     * Статус идеи после рассмотрения
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "idea_status")
    @NotNull(message = "Idea status is required")
    private IdeaStatus ideaStatus;
}