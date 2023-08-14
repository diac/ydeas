package com.diac.ydeas.notifications.service;

import com.diac.ydeas.domain.dto.IdeaReviewNotificationDto;
import com.diac.ydeas.domain.enumeration.NotificationStatus;
import com.diac.ydeas.domain.model.Notification;
import com.diac.ydeas.notifications.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    /**
     * Шаблон заголовка уведомления о рассмотрении идеи
     */
    private static final String REVIEW_NOTIFICATION_TITLE = "Your idea '%s' has been reviewed";

    /**
     * Шаблон содержания уведомления о рассмотрении идеи
     */
    private static final String REVIEW_NOTIFICATION_CONTENT = """
            Your idea '%s' has been reviewed and now has status %s.
            """;

    /**
     * Репозиторий для хранения объектов Notification
     */
    private final NotificationRepository notificationRepository;

    /**
     * Добавить в систему новое уведомление
     *
     * @param ideaReviewNotificationDto DTO с данными уведомления
     * @return Сохраненное уведомление
     */
    @Override
    public Notification add(IdeaReviewNotificationDto ideaReviewNotificationDto) {
        return notificationRepository.save(
                Notification.builder()
                        .title(String.format(REVIEW_NOTIFICATION_TITLE, ideaReviewNotificationDto.ideaTitle()))
                        .content(
                                String.format(
                                        REVIEW_NOTIFICATION_CONTENT,
                                        ideaReviewNotificationDto.ideaTitle(),
                                        ideaReviewNotificationDto.ideaStatus()
                                )
                        )
                        .recipientUuid(ideaReviewNotificationDto.ideaAuthorUuid())
                        .createdAt(LocalDateTime.now())
                        .notificationStatus(NotificationStatus.CREATED)
                        .build()
        );
    }

    @Override
    public void send(Notification notification) {

    }
}