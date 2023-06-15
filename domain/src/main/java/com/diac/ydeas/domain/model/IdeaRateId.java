package com.diac.ydeas.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.io.Serializable;

/**
 * Составной ID для сущности IdeaRate
 */
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class IdeaRateId implements Serializable {

    /**
     * Идентификатор идеи
     */
    @ManyToOne
    @JoinColumn(name = "idea_id")
    @EqualsAndHashCode.Include
    private Idea idea;

    /**
     * Идентификатор пользователя, поставившего оценку
     */
    @Column(name = "user_id")
    @EqualsAndHashCode.Include
    private int userId;
}