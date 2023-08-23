package com.diac.ydeas.notifications.service;

import com.diac.ydeas.domain.dto.IdeaRateNotificationDto;
import com.diac.ydeas.domain.dto.IdeaReviewNotificationDto;
import com.diac.ydeas.domain.enumeration.IdeaStatus;
import com.diac.ydeas.domain.enumeration.Rate;
import com.diac.ydeas.notifications.repository.NotificationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.UUID;

@SpringBootTest(classes = {
        NotificationServiceImpl.class
})
public class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @MockBean
    private NotificationRepository notificationRepository;

    @Test
    public void whenEnqueueIdeaReviewNotificationDto() {
        IdeaReviewNotificationDto ideaReviewNotificationDto = new IdeaReviewNotificationDto(
                1,
                "Test",
                UUID.randomUUID(),
                IdeaStatus.APPROVED
        );
        Assertions.assertAll(
                () -> notificationService.enqueue(ideaReviewNotificationDto)
        );
    }

    @Test
    public void whenEnqueueIdeaRateNotificationDto() {
        IdeaRateNotificationDto ideaRateNotificationDto = new IdeaRateNotificationDto(
                1,
                "Test",
                UUID.randomUUID(),
                Rate.LIKE
        );
        Assertions.assertAll(
                () -> notificationService.enqueue(ideaRateNotificationDto)
        );
    }
}