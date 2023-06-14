package com.diac.ydeas.domain.model;

import com.diac.ydeas.domain.enumeration.Rate;
import lombok.*;

/**
 * Модель данных "Оценка идеи"
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class IdeaRate {

    /**
     * Идея
     */
    @EqualsAndHashCode.Include
    private Idea idea;

    /**
     * Пользователь, поставивший оценку
     */
    @EqualsAndHashCode.Include
    private User user;

    /**
     * Оценка
     */
    private Rate rate;
}