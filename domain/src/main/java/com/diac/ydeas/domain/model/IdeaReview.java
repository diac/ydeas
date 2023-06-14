package com.diac.ydeas.domain.model;

import com.diac.ydeas.domain.enumeration.IdeaStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Модель данных "Рассмотрение идеи"
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class IdeaReview {

    /**
     * Идея
     */
    @EqualsAndHashCode.Include
    private Idea idea;

    /**
     * Пользователь-эксперт, рассмотревший идею
     */
    @NotNull(message = "Idea reviewer is required")
    private User reviewer;

    /**
     * Статус идеи после рассмотрения
     */
    @NotNull(message = "Idea status is required")
    private IdeaStatus ideaStatus;
}