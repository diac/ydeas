package com.diac.ydeas.notifications.service;

import com.diac.ydeas.domain.dto.IdeaRateNotificationDto;
import com.diac.ydeas.domain.dto.IdeaReviewNotificationDto;

/**
 * Сервис для работы с объектами Notification
 */
public interface NotificationService {

    /**
     * Поместить уведомление о рассмотрении идеи в очередь на отправку
     *
     * @param ideaReviewNotificationDto DTO с данными уведомления о рассмотрении идеи
     */
    void enqueue(IdeaReviewNotificationDto ideaReviewNotificationDto);

    /**
     * Поместить уведомление об оценке идеи в очередь на отправку
     *
     * @param ideaRateNotificationDto DTO с данными уведомления об оценке идеи
     */
    void enqueue(IdeaRateNotificationDto ideaRateNotificationDto);
}