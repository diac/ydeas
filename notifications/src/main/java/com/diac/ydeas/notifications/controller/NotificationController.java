package com.diac.ydeas.notifications.controller;

import com.diac.ydeas.domain.dto.IdeaRateNotificationDto;
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
     * Тема Kafka для обмена сообщениями об оценках идей
     */
    private static final String IDEA_RATE_KAFKA_NOTIFICATION_TOPIC = "idea-rate-notification";

    /**
     * Сервис для работы с объектами Notification
     */
    private final NotificationService notificationService;

    /**
     * Метод-слушатель Kafka-темы IDEA_REVIEW_KAFKA_NOTIFICATION_TOPIC. При получении сообщения из темы помещает
     * полученный объект в очередь для обработки в NotificationService
     *
     * @param message Сообщение IdeaReviewNotificationDto
     */
    @KafkaListener(
            topics = {IDEA_REVIEW_KAFKA_NOTIFICATION_TOPIC},
            containerFactory = "ideaReviewKafkaListenerContainerFactory"
    )
    public void onIdeaReviewMessage(ConsumerRecord<Integer, IdeaReviewNotificationDto> message) {
        notificationService.enqueue(message.value());
    }

    /**
     * Метод-слушатель Kafka-темы IDEA_REVIEW_KAFKA_NOTIFICATION_TOPIC. При получении сообщения из темы помещает
     * полученный объект в очередь для обработки в NotificationService
     *
     * @param message Сообщение IdeaRateNotificationDto
     */
    @KafkaListener(
            topics = {IDEA_RATE_KAFKA_NOTIFICATION_TOPIC},
            containerFactory = "ideaRateKafkaListenerContainerFactory"
    )
    public void onIdeaRateMessage(ConsumerRecord<Integer, IdeaRateNotificationDto> message) {
        notificationService.enqueue(message.value());
    }
}