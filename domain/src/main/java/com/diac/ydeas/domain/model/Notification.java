package com.diac.ydeas.domain.model;

import com.diac.ydeas.domain.enumeration.NotificationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Модель данных "Уведомление"
 */
@Entity
@Table(name = "notification")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Notification {

    /**
     * Идентификатор уведомления
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private long id;

    /**
     * Заголовок уведомления
     */
    @NotNull(message = "Notification title is required")
    @NotNull(message = "Notification title cannot be blank")
    private String title;

    /**
     * Содержание уведомления
     */
    @NotNull(message = "Notification title is required")
    @NotNull(message = "Notification title cannot be blank")
    private String content;

    /**
     * UUID получателя уведомления
     */
    @NotNull(message = "Recipient UUID is required")
    @NotNull(message = "Recipient UUID cannot be blank")
    @Column(name = "recipient_uuid")
    private UUID recipientUuid;

    /**
     * Дата и время создания уведомления
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Статус уведомления
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private NotificationStatus notificationStatus;
}