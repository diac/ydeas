package com.diac.ydeas.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Составной ID для сущности IdeaRate
 */
@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IdeaRateId implements Serializable {

    /**
     * Идентификатор идеи
     */
    @Column(name = "idea_id")
    private int ideaId;

    /**
     * Идентификатор пользователя, поставившего оценку
     */
    @Column(name = "user_id")
    private int userId;
}