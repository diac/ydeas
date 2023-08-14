package com.diac.ydeas.notifications.service;

import com.diac.ydeas.domain.dto.IdeaReviewNotificationDto;
import com.diac.ydeas.domain.model.Notification;

/**
 * Сервис для работы с объектами Notification
 */
public interface NotificationService {

    /**
     * Добавить в систему новое уведомление
     *
     * @param ideaReviewNotificationDto DTO с данными уведомления
     * @return Сохраненное уведомление
     */
    Notification add(IdeaReviewNotificationDto ideaReviewNotificationDto);

    /**
     * Отправить уведомление получателю
     *
     * @param notification Уведомление
     */
    void send(Notification notification);
}