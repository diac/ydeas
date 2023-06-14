package com.diac.ydeas.domain.model;

import com.diac.ydeas.domain.enumeration.Rate;
import jakarta.persistence.*;
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
    private Rate rate;
}