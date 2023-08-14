package com.diac.ydeas.notifications.repository;

import com.diac.ydeas.domain.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для хранения объектов Notification
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}