package com.diac.ydeas.notifications.service;

import com.diac.ydeas.domain.dto.IdeaRateNotificationDto;
import com.diac.ydeas.domain.dto.IdeaReviewNotificationDto;
import com.diac.ydeas.domain.enumeration.NotificationStatus;
import com.diac.ydeas.domain.enumeration.Rate;
import com.diac.ydeas.domain.model.Notification;
import com.diac.ydeas.notifications.repository.NotificationRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    // Количество нитей, отвечающих за отправку уведомлений
    private static final int NOTIFICATION_THREADS_NUMBER = Runtime.getRuntime().availableProcessors();

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
     * Шаблон заголовка уведомления об оценке идеи
     */
    private static final String RATE_NOTIFICATION_TITLE = "Your idea '%s' has been rated";

    /**
     * Шаблоны содержания уведомления об оценке идеи
     */
    private static final Map<Rate, String> RATE_NOTIFICATION_CONTENT = Map.ofEntries(
            Map.entry(Rate.LIKE, "Someone liked your idea #%s"),
            Map.entry(Rate.DISLIKE, "Someone disliked your idea #%s")
    );

    // Интервал повторной отправки уведомлений
    private static final long PROCESS_INTERVAL = 15_000;

    // Вероятность успешной отправки уведомления от 0 до 1
    private static final double SUCCESS_RATE = 0.5;

    /**
     * Репозиторий для хранения объектов Notification
     */
    private final NotificationRepository notificationRepository;

    // Очередь уведомлений на отправку
    private final Deque<Notification> notificationsQueue = new ConcurrentLinkedDeque<>();

    /**
     * Метод PostConstruct бина
     */
    @PostConstruct
    private void postConstruct() {
        runNotificationDeliverySchedule();
    }

    /**
     * Поместить уведомление о рассмотрении идеи в очередь на отправку
     *
     * @param ideaReviewNotificationDto DTO с данными уведомления о рассмотрении идеи
     */
    @Override
    public void enqueue(IdeaReviewNotificationDto ideaReviewNotificationDto) {
        Notification notification = Notification.builder()
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
                .build();
        notificationRepository.save(notification);
        notificationsQueue.offer(notification);
    }

    /**
     * Поместить уведомление об оценке идеи в очередь на отправку
     *
     * @param ideaRateNotificationDto DTO с данными уведомления об оценке идеи
     */
    @Override
    public void enqueue(IdeaRateNotificationDto ideaRateNotificationDto) {
        Notification notification = Notification.builder()
                .title(String.format(RATE_NOTIFICATION_TITLE, ideaRateNotificationDto.ideaTitle()))
                .content(
                        String.format(
                                RATE_NOTIFICATION_CONTENT.get(ideaRateNotificationDto.rate()),
                                ideaRateNotificationDto.ideaTitle()
                        )
                )
                .recipientUuid(ideaRateNotificationDto.ideaAuthorUuid())
                .createdAt(LocalDateTime.now())
                .notificationStatus(NotificationStatus.CREATED)
                .build();
        notificationRepository.save(notification);
        notificationsQueue.offer(notification);
    }

    /**
     * Запустить процесс периодической отправки уведомлений
     */
    private void runNotificationDeliverySchedule() {
        ScheduledExecutorService schedulerService = Executors.newScheduledThreadPool(1);
        ExecutorService workerExecutorService = Executors.newFixedThreadPool(NOTIFICATION_THREADS_NUMBER);
        schedulerService.scheduleAtFixedRate(
                () -> {
                    for (int i = 0; i < NOTIFICATION_THREADS_NUMBER; i++) {
                        workerExecutorService.execute(this::processNotificationDelivery);
                    }
                },
                0,
                PROCESS_INTERVAL,
                TimeUnit.MILLISECONDS
        );
    }

    /**
     * Выполнить работу по отправке уведомлений
     */
    private void processNotificationDelivery() {
        if (notificationsQueue.isEmpty()) {
            return;
        }
        Notification notification = notificationsQueue.poll();
        if (send(notification)) {
            notification.setNotificationStatus(NotificationStatus.SENT);
            notificationRepository.save(notification);
        } else {
            notification.setNotificationStatus(NotificationStatus.FAILED);
            notificationRepository.save(notification);
            notificationsQueue.offerFirst(notification);
        }
    }

    /**
     * Отправить уведомление получателю. В учебных целях успешная отправка срабатывает с вероятностью SUCCESS_RATE
     *
     * @param notification Уведомление
     * @return true -- если удалось отправить уведомление, иначе -- false
     */
    public boolean send(Notification notification) {
        if (Math.random() < SUCCESS_RATE) {
            System.out.printf("Notification #%s has been sent%n", notification.getId());
            return true;
        } else {
            System.out.printf("Failed to send notification #%s%n", notification.getId());
            return false;
        }
    }
}