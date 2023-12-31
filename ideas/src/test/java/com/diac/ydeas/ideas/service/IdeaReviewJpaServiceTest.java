package com.diac.ydeas.ideas.service;

import com.diac.ydeas.domain.dto.IdeaReviewNotificationDto;
import com.diac.ydeas.domain.enumeration.IdeaStatus;
import com.diac.ydeas.domain.exception.ResourceConstraintViolationException;
import com.diac.ydeas.domain.exception.ResourceNotFoundException;
import com.diac.ydeas.domain.model.Idea;
import com.diac.ydeas.domain.model.IdeaReview;
import com.diac.ydeas.ideas.repository.IdeaReviewRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {
        IdeaReviewJpaService.class
})
public class IdeaReviewJpaServiceTest {

    private static final Idea IDEA_TEMPLATE;

    static {
        int number = 1;
        String value = String.valueOf(System.currentTimeMillis());
        UUID uuid = UUID.randomUUID();
        IDEA_TEMPLATE = Idea.builder()
                .id(number)
                .title(value)
                .description(value)
                .authorUuid(uuid)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Autowired
    private IdeaReviewService ideaReviewService;

    @MockBean
    private IdeaReviewRepository ideaReviewRepository;

    @MockBean
    private IdeaService ideaService;

    @MockBean
    private KafkaTemplate<Integer, IdeaReviewNotificationDto> ideaReviewKafkaTemplate;

    @Test
    public void whenFindAll() {
        List<IdeaReview> expectedIdeaReviews = List.of(
                IdeaReview.builder()
                        .idea(IDEA_TEMPLATE)
                        .build()
        );
        Mockito.when(ideaReviewRepository.findAll()).thenReturn(expectedIdeaReviews);
        List<IdeaReview> ideaReviews = ideaReviewService.findAll();
        Mockito.verify(ideaReviewRepository).findAll();
        assertThat(ideaReviews).isEqualTo(expectedIdeaReviews);
    }

    @Test
    public void whenGetPage() {
        List<IdeaReview> expectedIdeaReviews = List.of(
                IdeaReview.builder()
                        .idea(IDEA_TEMPLATE)
                        .build()
        );
        Page<IdeaReview> expectedIdeaReviewPage = Mockito.mock(Page.class);
        Mockito.when(expectedIdeaReviewPage.getContent()).thenReturn(expectedIdeaReviews);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Mockito.when(ideaReviewRepository.findAll(pageRequest)).thenReturn(expectedIdeaReviewPage);
        assertThat(ideaReviewService.getPage(pageRequest).getContent()).isEqualTo(expectedIdeaReviews);
        Mockito.verify(ideaReviewRepository).findAll(pageRequest);
    }

    @Test
    public void whenFindAllByReviewerUserUuid() {
        UUID uuid = UUID.randomUUID();
        List<IdeaReview> expectedIdeaReviews = List.of(
                IdeaReview.builder()
                        .idea(IDEA_TEMPLATE)
                        .reviewerUserUuid(uuid)
                        .build()
        );
        Mockito.when(ideaReviewRepository.findAllByReviewerUserUuid(uuid)).thenReturn(expectedIdeaReviews);
        List<IdeaReview> ideaReviews = ideaReviewService.findAllByReviewerUserUuid(uuid);
        assertThat(ideaReviews).isEqualTo(expectedIdeaReviews);
        Mockito.verify(ideaReviewRepository).findAllByReviewerUserUuid(uuid);
    }

    @Test
    public void whenFindAllByIdeaStatus() {
        IdeaStatus ideaStatus = IdeaStatus.APPROVED;
        List<IdeaReview> expectedIdeaReviews = List.of(
                IdeaReview.builder()
                        .idea(IDEA_TEMPLATE)
                        .ideaStatus(ideaStatus)
                        .build()
        );
        Mockito.when(ideaReviewRepository.findAllByIdeaStatus(ideaStatus)).thenReturn(expectedIdeaReviews);
        List<IdeaReview> ideaReviews = ideaReviewService.findAllByIdeaStatus(ideaStatus);
        assertThat(ideaReviews).isEqualTo(expectedIdeaReviews);
        Mockito.verify(ideaReviewRepository).findAllByIdeaStatus(ideaStatus);
    }

    @Test
    public void whenFindByIdFound() {
        int ideaId = IDEA_TEMPLATE.getId();
        IdeaReview ideaReview = IdeaReview.builder()
                .idea(IDEA_TEMPLATE)
                .build();
        Mockito.when(ideaReviewRepository.findById(ideaId)).thenReturn(Optional.of(ideaReview));
        assertThat(ideaReviewService.findByIdeaId(ideaId)).isEqualTo(ideaReview);
        Mockito.verify(ideaReviewRepository).findById(ideaId);
    }

    @Test
    public void whenFindByIdNotFoundThenThrowException() {
        int ideaId = IDEA_TEMPLATE.getId();
        Mockito.when(ideaReviewRepository.findById(ideaId)).thenReturn(Optional.empty());
        assertThatThrownBy(
                () -> ideaReviewService.findByIdeaId(ideaId)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void whenAdd() {
        IdeaReview ideaReview = IdeaReview.builder()
                .idea(IDEA_TEMPLATE)
                .build();
        Mockito.when(ideaReviewRepository.save(ideaReview)).thenReturn(ideaReview);
        IdeaReview savedIdeaReview = ideaReviewService.add(ideaReview);
        Mockito.verify(ideaReviewRepository).save(ideaReview);
        assertThat(savedIdeaReview).isEqualTo(ideaReview);
    }

    @Test
    public void whenAddViolatesConstraintsThenThrowException() {
        IdeaReview ideaReview = IdeaReview.builder()
                .idea(IDEA_TEMPLATE)
                .build();
        Mockito.when(ideaReviewRepository.save(ideaReview))
                .thenThrow(ConstraintViolationException.class);
        assertThatThrownBy(
                () -> ideaReviewService.add(ideaReview)
        ).isInstanceOf(ResourceConstraintViolationException.class);
    }

    @Test
    public void whenUpdate() {
        int ideaId = IDEA_TEMPLATE.getId();
        IdeaReview ideaReview = IdeaReview.builder()
                .idea(IDEA_TEMPLATE)
                .build();
        Mockito.when(ideaReviewRepository.findById(ideaId)).thenReturn(Optional.of(ideaReview));
        Mockito.when(ideaReviewRepository.save(ideaReview)).thenReturn(ideaReview);
        IdeaReview updatedIdeaReview = ideaReviewService.update(ideaId, ideaReview);
        Mockito.verify(ideaReviewRepository).findById(ideaId);
        Mockito.verify(ideaReviewRepository).save(ideaReview);
        assertThat(ideaReview).isEqualTo(updatedIdeaReview);
    }

    @Test
    public void whenUpdateNonExistentThenThrowException() {
        int ideaId = IDEA_TEMPLATE.getId();
        IdeaReview ideaReview = IdeaReview.builder()
                .idea(IDEA_TEMPLATE)
                .build();
        Mockito.when(ideaReviewRepository.findById(ideaId)).thenReturn(Optional.empty());
        assertThatThrownBy(
                () -> ideaReviewService.update(ideaId, ideaReview)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void whenUpdateViolatesDataIntegrityThenThrowException() {
        int ideaId = IDEA_TEMPLATE.getId();
        IdeaReview ideaReview = IdeaReview.builder()
                .idea(IDEA_TEMPLATE)
                .build();
        Mockito.when(ideaReviewRepository.findById(ideaId)).thenReturn(Optional.of(ideaReview));
        Mockito.when(ideaReviewRepository.save(ideaReview))
                .thenThrow(DataIntegrityViolationException.class);
        assertThatThrownBy(
                () -> ideaReviewService.update(ideaId, ideaReview)
        ).isInstanceOf(ResourceConstraintViolationException.class);
    }

    @Test
    public void whenUpdateViolatesConstraintsThenThrowException() {
        int ideaId = IDEA_TEMPLATE.getId();
        IdeaReview ideaReview = IdeaReview.builder()
                .idea(IDEA_TEMPLATE)
                .build();
        Mockito.when(ideaReviewRepository.findById(ideaId)).thenReturn(Optional.of(ideaReview));
        Mockito.when(ideaReviewRepository.save(ideaReview))
                .thenThrow(ConstraintViolationException.class);
        assertThatThrownBy(
                () -> ideaReviewService.update(ideaId, ideaReview)
        ).isInstanceOf(ResourceConstraintViolationException.class);
    }

    @Test
    public void whenDelete() {
        int ideaId = IDEA_TEMPLATE.getId();
        IdeaReview ideaReview = IdeaReview.builder()
                .idea(IDEA_TEMPLATE)
                .build();
        Mockito.when(ideaReviewRepository.findById(ideaId)).thenReturn(Optional.of(ideaReview));
        ideaReviewService.delete(ideaId);
        Mockito.verify(ideaReviewRepository).findById(ideaId);
        Mockito.verify(ideaReviewRepository).deleteById(ideaId);
    }

    @Test
    public void whenDeleteNonExistentThenThrowException() {
        int ideaId = IDEA_TEMPLATE.getId();
        Mockito.when(ideaReviewRepository.findById(ideaId)).thenReturn(Optional.empty());
        assertThatThrownBy(
                () -> ideaReviewService.delete(ideaId)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void whenApprove() {
        int id = 1;
        UUID uuid = UUID.randomUUID();
        Idea idea = Idea.builder()
                .id(id)
                .build();
        IdeaReview ideaReview = IdeaReview.builder()
                .ideaId(id)
                .idea(idea)
                .reviewerUserUuid(uuid)
                .ideaStatus(IdeaStatus.APPROVED)
                .build();
        Mockito.when(ideaService.findById(id)).thenReturn(idea);
        Assertions.assertAll(
                () -> ideaReviewService.approve(id, uuid)
        );
        Mockito.verify(ideaReviewRepository).save(ideaReview);
    }

    @Test
    public void whenDecline() {
        int id = 1;
        UUID uuid = UUID.randomUUID();
        Idea idea = Idea.builder()
                .id(id)
                .build();
        IdeaReview ideaReview = IdeaReview.builder()
                .ideaId(id)
                .idea(idea)
                .reviewerUserUuid(uuid)
                .ideaStatus(IdeaStatus.DECLINED)
                .build();
        Mockito.when(ideaService.findById(id)).thenReturn(idea);
        Assertions.assertAll(
                () -> ideaReviewService.decline(id, uuid)
        );
        Mockito.verify(ideaReviewRepository).save(ideaReview);
    }
}