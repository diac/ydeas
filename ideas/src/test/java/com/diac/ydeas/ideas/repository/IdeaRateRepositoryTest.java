package com.diac.ydeas.ideas.repository;

import com.diac.ydeas.domain.enumeration.Rate;
import com.diac.ydeas.domain.model.Idea;
import com.diac.ydeas.domain.model.IdeaRate;
import com.diac.ydeas.domain.model.IdeaRateId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class IdeaRateRepositoryTest extends AbstractRepositoryIntegrationTest {

    private static final Idea IDEA;

    static {
        int number = 1;
        UUID uuid = UUID.randomUUID();
        String value = String.valueOf(System.currentTimeMillis());
        IDEA = Idea.builder()
                .title(value)
                .description(value)
                .authorUuid(uuid)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Autowired
    private IdeaRateRepository ideaRateRepository;

    @Autowired
    private IdeaRepository ideaRepository;

    @BeforeEach
    public void init() {
        IDEA.setId(ideaRepository.save(IDEA).getId());
    }

    @Test
    public void whenFindAll() {
        int number = 1;
        Rate rate = Rate.LIKE;
        IdeaRate ideaRate = ideaRateRepository.save(
                IdeaRate.builder()
                        .ideaRateId(
                                IdeaRateId.builder()
                                        .idea(IDEA)
                                        .userId(number)
                                        .build()
                        )
                        .rate(rate)
                        .build()
        );
        assertThat(ideaRateRepository.findAll()).contains(ideaRate);
    }

    @Test
    public void whenFindById() {
        int number = 1;
        Rate rate = Rate.LIKE;
        IdeaRate ideaRate = ideaRateRepository.save(
                IdeaRate.builder()
                        .ideaRateId(
                                IdeaRateId.builder()
                                        .idea(IDEA)
                                        .userId(number)
                                        .build()
                        )
                        .rate(rate)
                        .build()
        );
        IdeaRate ideaRateInDb = ideaRateRepository.findById(ideaRate.getIdeaRateId())
                .orElse(new IdeaRate());
        assertThat(ideaRateInDb).isEqualTo(ideaRate);
    }

    @Test
    public void whenFindAllByIdeaRateIdIdeaId() {
        int number = 1;
        Rate rate = Rate.LIKE;
        IdeaRate ideaRate = ideaRateRepository.save(
                IdeaRate.builder()
                        .ideaRateId(
                                IdeaRateId.builder()
                                        .idea(IDEA)
                                        .userId(number)
                                        .build()
                        )
                        .rate(rate)
                        .build()
        );
        List<IdeaRate> ideaRates = ideaRateRepository.findAllByIdeaRateIdIdeaId(IDEA.getId());
        assertThat(ideaRates).contains(ideaRate);
    }

    @Test
    public void whenALlByIdeaRateIdUserId() {
        int number = 1;
        Rate rate = Rate.LIKE;
        IdeaRate ideaRate = ideaRateRepository.save(
                IdeaRate.builder()
                        .ideaRateId(
                                IdeaRateId.builder()
                                        .idea(IDEA)
                                        .userId(number)
                                        .build()
                        )
                        .rate(rate)
                        .build()
        );
        List<IdeaRate> ideaRates = ideaRateRepository.findAllByIdeaRateIdUserId(number);
        assertThat(ideaRates).contains(ideaRate);
    }

    @Test
    public void whenAdd() {
        int number = 1;
        Rate rate = Rate.LIKE;
        IdeaRate ideaRate = ideaRateRepository.save(
                IdeaRate.builder()
                        .ideaRateId(
                                IdeaRateId.builder()
                                        .idea(IDEA)
                                        .userId(number)
                                        .build()
                        )
                        .rate(rate)
                        .build()
        );
        assertThat(rate).isEqualTo(ideaRate.getRate());
    }

    @Test
    public void whenUpdate() {
        int number = 1;
        Rate rate = Rate.LIKE;
        Rate newRate = Rate.DISLIKE;
        IdeaRate ideaRate = ideaRateRepository.save(
                IdeaRate.builder()
                        .ideaRateId(
                                IdeaRateId.builder()
                                        .idea(IDEA)
                                        .userId(number)
                                        .build()
                        )
                        .rate(rate)
                        .build()
        );
        ideaRate.setRate(newRate);
        IdeaRate updatedIdeaRate = ideaRateRepository.save(ideaRate);
        assertThat(ideaRate).isEqualTo(updatedIdeaRate);
        assertThat(ideaRate.getRate()).isEqualTo(updatedIdeaRate.getRate());
    }

    @Test
    public void whenDelete() {
        int number = 1;
        Rate rate = Rate.LIKE;
        IdeaRate ideaRate = ideaRateRepository.save(
                IdeaRate.builder()
                        .ideaRateId(
                                IdeaRateId.builder()
                                        .idea(IDEA)
                                        .userId(number)
                                        .build()
                        )
                        .rate(rate)
                        .build()
        );
        ideaRateRepository.delete(ideaRate);
        assertThat(ideaRateRepository.findAll()).doesNotContain(ideaRate);
    }
}