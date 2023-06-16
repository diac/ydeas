package com.diac.ydeas.domain.model;

import com.diac.ydeas.domain.enumeration.IdeaStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

/**
 * Модель данных "Рейтинг идей"
 */
@Entity
@Immutable
@Table(name = "idea_rating")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class IdeaRating {

    /**
     * Идентификатор идеи
     */
    @Id
    @Column(name = "idea_id", updatable = false, nullable = false)
    private int ideaId;

    /**
     * Заголовок идеи
     */
    @Column(name = "idea_title")
    private String ideaTitle;

    /**
     * Идентификатор пользователя-автора идеи
     */
    @Column(name = "idea_author_user_id")
    private int ideaAuthorId;

    /**
     * Дата и время создания идеи
     */
    @Column(name = "idea_created_at")
    private LocalDateTime ideaCreatedAt;

    /**
     * Количество лайков
     */
    @Column(name = "likes")
    private int likes;

    /**
     * Количество дизлайков
     */
    @Column(name = "dislikes")
    private int dislikes;

    /**
     * Рейтинг идеи
     */
    @Column(name = "rating")
    private int rating;

    @Column(name = "idea_status")
    @Enumerated(EnumType.STRING)
    private IdeaStatus ideaStatus;
}