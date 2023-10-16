package com.diac.ydeas.domain.model;

import com.diac.ydeas.domain.enumeration.Rate;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Модель данных \"Оценка идеи\"")
public class IdeaRate {

    /**
     * Идентификатор оценки идеи
     */
    @EmbeddedId
    @EqualsAndHashCode.Include
    @Schema(description = "Идентификатор оценки идеи", example = "1")
    private IdeaRateId ideaRateId;

    /**
     * Оценка
     */
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Idea rate is required")
    @Schema(description = "Оценка", example = "LIKE")
    private Rate rate;
}