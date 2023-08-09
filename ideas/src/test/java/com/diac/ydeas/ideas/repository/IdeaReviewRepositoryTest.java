package com.diac.ydeas.ideas.repository;

import com.diac.ydeas.domain.enumeration.IdeaStatus;
import com.diac.ydeas.domain.model.Idea;
import com.diac.ydeas.domain.model.IdeaReview;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class IdeaReviewRepositoryTest extends AbstractRepositoryIntegrationTest {

    private static final Idea IDEA_TEMPLATE;

    static {
        String value = String.valueOf(System.currentTimeMillis());
        UUID uuid = UUID.randomUUID();
        IDEA_TEMPLATE = Idea.builder()
                .title(value)
                .description(value)
                .authorUuid(uuid)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Autowired
    private IdeaReviewRepository ideaReviewRepository;

    @Autowired
    private IdeaRepository ideaRepository;

    private Idea idea;

    @BeforeEach
    public void init() {
        idea = ideaRepository.save(IDEA_TEMPLATE);
    }

    @Test
    public void whenFindAll() {
        int number = 1;
        IdeaStatus ideaStatus = IdeaStatus.APPROVED;
        IdeaReview ideaReview = ideaReviewRepository.save(
                IdeaReview.builder()
                        .idea(idea)
                        .ideaStatus(ideaStatus)
                        .reviewerUserId(number)
                        .build()
        );
        assertThat(ideaReviewRepository.findAll()).contains(ideaReview);
    }

    @Test
    public void whenFindById() {
        int number = 1;
        IdeaStatus ideaStatus = IdeaStatus.APPROVED;
        IdeaReview ideaReview = ideaReviewRepository.save(
                IdeaReview.builder()
                        .idea(idea)
                        .ideaStatus(ideaStatus)
                        .reviewerUserId(number)
                        .build()
        );
        IdeaReview ideaReviewInDb = ideaReviewRepository.findById(idea.getId())
                .orElse(new IdeaReview());
        assertThat(ideaReviewInDb).isEqualTo(ideaReview);
    }

    @Test
    public void whenFindAllByReviewerUserId() {
        int number = 1;
        IdeaStatus ideaStatus = IdeaStatus.APPROVED;
        IdeaReview ideaReview = ideaReviewRepository.save(
                IdeaReview.builder()
                        .idea(idea)
                        .ideaStatus(ideaStatus)
                        .reviewerUserId(number)
                        .build()
        );
        List<IdeaReview> ideaReviews = ideaReviewRepository.findAllByReviewerUserId(number);
        assertThat(ideaReviews).contains(ideaReview);
    }

    @Test
    public void whenFindAllByIdeaStatus() {
        int number = 1;
        IdeaStatus ideaStatus = IdeaStatus.APPROVED;
        IdeaReview ideaReview = ideaReviewRepository.save(
                IdeaReview.builder()
                        .idea(idea)
                        .ideaStatus(ideaStatus)
                        .reviewerUserId(number)
                        .build()
        );
        List<IdeaReview> ideaReviews = ideaReviewRepository.findAllByIdeaStatus(ideaStatus);
        assertThat(ideaReviews).contains(ideaReview);
    }

    @Test
    public void wheAdd() {
        int number = 1;
        IdeaStatus ideaStatus = IdeaStatus.APPROVED;
        IdeaReview ideaReview = ideaReviewRepository.save(
                IdeaReview.builder()
                        .idea(idea)
                        .ideaStatus(ideaStatus)
                        .reviewerUserId(number)
                        .build()
        );
        assertThat(ideaStatus).isEqualTo(ideaReview.getIdeaStatus());
        assertThat(number).isEqualTo(ideaReview.getReviewerUserId());
    }

    @Test
    public void whenUpdate() {
        int number = 1;
        IdeaStatus ideaStatus = IdeaStatus.APPROVED;
        IdeaStatus newStatus = IdeaStatus.DECLINED;
        IdeaReview ideaReview = ideaReviewRepository.save(
                IdeaReview.builder()
                        .idea(idea)
                        .ideaStatus(ideaStatus)
                        .reviewerUserId(number)
                        .build()
        );
        ideaReview.setIdeaStatus(newStatus);
        IdeaReview updatedIdeaReview = ideaReviewRepository.save(ideaReview);
        assertThat(ideaReview).isEqualTo(updatedIdeaReview);
        assertThat(ideaReview.getIdeaStatus()).isEqualTo(updatedIdeaReview.getIdeaStatus());
    }

    @Test
    public void whenDelete() {
        int number = 1;
        IdeaStatus ideaStatus = IdeaStatus.APPROVED;
        IdeaReview ideaReview = ideaReviewRepository.save(
                IdeaReview.builder()
                        .idea(idea)
                        .ideaStatus(ideaStatus)
                        .reviewerUserId(number)
                        .build()
        );
        ideaReviewRepository.delete(ideaReview);
        assertThat(ideaReviewRepository.findAll()).doesNotContain(ideaReview);
    }
}