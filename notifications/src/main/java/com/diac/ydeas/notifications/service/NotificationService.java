package com.diac.ydeas.notifications.service;

import com.diac.ydeas.domain.dto.IdeaReviewNotificationDto;

/**
 * Сервис для работы с объектами Notification
 */
public interface NotificationService {

    /**
     * Поместить уведомление в очередь на отправку
     *
     * @param ideaReviewNotificationDto DTO с данными уведомления
     */
    void enqueue(IdeaReviewNotificationDto ideaReviewNotificationDto);
}