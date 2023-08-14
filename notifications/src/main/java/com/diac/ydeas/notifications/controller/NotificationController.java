package com.diac.ydeas.notifications.controller;

import com.diac.ydeas.domain.dto.IdeaReviewNotificationDto;
import com.diac.ydeas.notifications.service.NotificationService;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

/**
 * Контроллер для работы с объектами Notification
 */
@Controller
@AllArgsConstructor
public class NotificationController {

    /**
     * Тема Kafka для обмена сообщениями о рассмотрении идей
     */
    private static final String IDEA_REVIEW_KAFKA_NOTIFICATION_TOPIC = "idea-review-notification";

    /**
     * Сервис для работы с объектами Notification
     */
    private final NotificationService notificationService;

    @KafkaListener(
            topics = {IDEA_REVIEW_KAFKA_NOTIFICATION_TOPIC},
            containerFactory = "ideaReviewKafkaListenerContainerFactory"
    )
    public void onIdeaReviewMessage(ConsumerRecord<Integer, IdeaReviewNotificationDto> message) {
        notificationService.add(message.value());
    }
}