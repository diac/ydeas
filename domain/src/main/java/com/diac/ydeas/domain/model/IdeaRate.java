package com.diac.ydeas.domain.model;

import com.diac.ydeas.domain.enumeration.Rate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Модель данных "Оценка идеи"
 */
@Entity
@Table(name = "idea_rate")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class IdeaRate {

    /**
     * Идентификатор оценки идеи
     */
    @EmbeddedId
    @EqualsAndHashCode.Include
    private IdeaRateId ideaRateId;

    /**
     * Идея
     */
    @MapsId("idea_id")
    @ManyToOne
    private Idea idea;

    /**
     * Оценка
     */
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Idea rate is required")
    private Rate rate;
}