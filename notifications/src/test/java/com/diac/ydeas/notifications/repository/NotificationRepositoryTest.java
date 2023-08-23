package com.diac.ydeas.notifications.repository;

import com.diac.ydeas.domain.enumeration.NotificationStatus;
import com.diac.ydeas.domain.model.Notification;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class NotificationRepositoryTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    public void whenFindAll() {
        String value = "test";
        UUID uuid = UUID.randomUUID();
        Notification notification = notificationRepository.save(
                Notification.builder()
                        .title(value)
                        .content(value)
                        .recipientUuid(uuid)
                        .createdAt(LocalDateTime.now())
                        .notificationStatus(NotificationStatus.CREATED)
                        .build()
        );
        assertThat(notificationRepository.findAll()).contains(notification);
    }

    @Test
    public void whenFindById() {
        String value = "test";
        UUID uuid = UUID.randomUUID();
        Notification notification = notificationRepository.save(
                Notification.builder()
                        .title(value)
                        .content(value)
                        .recipientUuid(uuid)
                        .createdAt(LocalDateTime.now())
                        .notificationStatus(NotificationStatus.CREATED)
                        .build()
        );
        Notification notificationInDb = notificationRepository.findById(notification.getId())
                .orElse(new Notification());
        assertThat(notificationInDb).isEqualTo(notification);
    }

    @Test
    public void whenAdd() {
        String value = "test";
        UUID uuid = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Notification notification = notificationRepository.save(
                Notification.builder()
                        .title(value)
                        .content(value)
                        .recipientUuid(uuid)
                        .createdAt(now)
                        .notificationStatus(NotificationStatus.CREATED)
                        .build()
        );
        assertThat(value).isEqualTo(notification.getTitle());
        assertThat(value).isEqualTo(notification.getContent());
        assertThat(uuid).isEqualTo(notification.getRecipientUuid());
        assertThat(now).isEqualTo(notification.getCreatedAt());
        assertThat(NotificationStatus.CREATED).isEqualTo(notification.getNotificationStatus());
    }

    @Test
    public void whenUpdate() {
        String value = "test";
        UUID uuid = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Notification notification = notificationRepository.save(
                Notification.builder()
                        .title(value)
                        .content(value)
                        .recipientUuid(uuid)
                        .createdAt(now)
                        .notificationStatus(NotificationStatus.CREATED)
                        .build()
        );
        notification.setTitle(notification.getTitle() + "_updated");
        notification.setContent(notification.getTitle() + "_updated");
        notification.setNotificationStatus(NotificationStatus.SENT);
        Notification updatedNotification = notificationRepository.save(notification);
        assertThat(notification).isEqualTo(updatedNotification);
        assertThat(notification.getTitle()).isEqualTo(updatedNotification.getTitle());
        assertThat(notification.getContent()).isEqualTo(updatedNotification.getContent());
        assertThat(notification.getNotificationStatus()).isEqualTo(updatedNotification.getNotificationStatus());
    }

    @Test
    public void whenDelete() {
        String value = "test";
        UUID uuid = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        Notification notification = notificationRepository.save(
                Notification.builder()
                        .title(value)
                        .content(value)
                        .recipientUuid(uuid)
                        .createdAt(now)
                        .notificationStatus(NotificationStatus.CREATED)
                        .build()
        );
        notificationRepository.delete(notification);
        assertThat(notificationRepository.findAll()).doesNotContain(notification);
    }
}