package com.diac.ydeas.domain.model;

import com.diac.ydeas.domain.enumeration.IdeaStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
    @OneToOne
    @JoinColumn(name = "idea_id")
    @MapsId
    private Idea idea;

    /**
     * Пользователь-эксперт, рассмотревший идею
     */
    @NotNull(message = "Idea reviewer ID is required")
    @Column(name = "reviewer_user_id")
    private Integer reviewerUserId;

    /**
     * Статус идеи после рассмотрения
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "idea_status")
    @NotNull(message = "Idea status is required")
    private IdeaStatus ideaStatus;
}