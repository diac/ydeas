package com.diac.ydeas.ideas.service;

import com.diac.ydeas.domain.dto.IdeaRateNotificationDto;
import com.diac.ydeas.domain.exception.ResourceConstraintViolationException;
import com.diac.ydeas.domain.exception.ResourceNotFoundException;
import com.diac.ydeas.domain.model.Idea;
import com.diac.ydeas.domain.model.IdeaRate;
import com.diac.ydeas.domain.model.IdeaRateId;
import com.diac.ydeas.ideas.repository.IdeaRateRepository;
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
        IdeaRateJpaService.class
})
public class IdeaRateJpaServiceTest {

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

    private static final IdeaRateId IDEA_RATE_ID = IdeaRateId.builder()
            .idea(IDEA_TEMPLATE)
            .userUuid(UUID.randomUUID())
            .build();

    @Autowired
    private IdeaRateService ideaRateService;

    @MockBean
    private IdeaRateRepository ideaRateRepository;

    @MockBean
    private KafkaTemplate<Integer, IdeaRateNotificationDto> kafkaTemplate;

    @MockBean
    private IdeaService ideaService;

    @Test
    public void whenFindAll() {
        List<IdeaRate> expectedIdeaRates = List.of(
                IdeaRate.builder()
                        .ideaRateId(IDEA_RATE_ID)
                        .build()
        );
        Mockito.when(ideaRateRepository.findAll()).thenReturn(expectedIdeaRates);
        List<IdeaRate> ideaRates = ideaRateService.findAll();
        Mockito.verify(ideaRateRepository).findAll();
        assertThat(ideaRates).isEqualTo(expectedIdeaRates);
    }

    @Test
    public void whenGetPage() {
        List<IdeaRate> expectedIdeaRates = List.of(
                IdeaRate.builder()
                        .ideaRateId(IDEA_RATE_ID)
                        .build()
        );
        Page<IdeaRate> expectedIdeaRatePage = Mockito.mock(Page.class);
        Mockito.when(expectedIdeaRatePage.getContent()).thenReturn(expectedIdeaRates);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Mockito.when(ideaRateRepository.findAll(pageRequest)).thenReturn(expectedIdeaRatePage);
        assertThat(ideaRateService.getPage(pageRequest).getContent()).isEqualTo(expectedIdeaRates);
        Mockito.verify(ideaRateRepository).findAll(pageRequest);
    }

    @Test
    public void whenFindAllByIdeaId() {
        int ideaId = IDEA_RATE_ID.getIdea().getId();
        List<IdeaRate> expectedIdeaRates = List.of(
                IdeaRate.builder()
                        .ideaRateId(IDEA_RATE_ID)
                        .build()
        );
        Mockito.when(ideaRateRepository.findAllByIdeaRateIdIdeaId(ideaId)).thenReturn(expectedIdeaRates);
        List<IdeaRate> ideaRates = ideaRateService.findAllByIdeaId(ideaId);
        assertThat(ideaRates).isEqualTo(expectedIdeaRates);
        Mockito.verify(ideaRateRepository).findAllByIdeaRateIdIdeaId(ideaId);
    }

    @Test
    public void whenFindAllByUserUuid() {
        UUID userUuid = IDEA_RATE_ID.getUserUuid();
        List<IdeaRate> expectedIdeaRates = List.of(
                IdeaRate.builder()
                        .ideaRateId(IDEA_RATE_ID)
                        .build()
        );
        Mockito.when(ideaRateRepository.findAllByIdeaRateIdUserUuid(userUuid)).thenReturn(expectedIdeaRates);
        List<IdeaRate> ideaRates = ideaRateService.findAllByUserUuid(userUuid);
        assertThat(ideaRates).isEqualTo(expectedIdeaRates);
        Mockito.verify(ideaRateRepository).findAllByIdeaRateIdUserUuid(userUuid);
    }

    @Test
    public void whenFindByIdFound() {
        IdeaRate ideaRate = IdeaRate.builder()
                .ideaRateId(IDEA_RATE_ID)
                .build();
        Mockito.when(ideaRateRepository.findById(IDEA_RATE_ID)).thenReturn(Optional.of(ideaRate));
        assertThat(ideaRateService.findById(IDEA_RATE_ID)).isEqualTo(ideaRate);
        Mockito.verify(ideaRateRepository).findById(IDEA_RATE_ID);
    }

    @Test
    public void whenFindByIdNotFoundThenThrowException() {
        Mockito.when(ideaRateRepository.findById(IDEA_RATE_ID)).thenReturn(Optional.empty());
        assertThatThrownBy(
                () -> ideaRateService.findById(IDEA_RATE_ID)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void whenAdd() {
        IdeaRate ideaRate = IdeaRate.builder()
                .ideaRateId(IDEA_RATE_ID)
                .build();
        Mockito.when(ideaRateRepository.save(ideaRate)).thenReturn(ideaRate);
        IdeaRate savedIdeaRate = ideaRateService.add(ideaRate);
        Mockito.verify(ideaRateRepository).save(ideaRate);
        assertThat(savedIdeaRate).isEqualTo(ideaRate);
    }

    @Test
    public void whenAddViolatesConstraintsThenThrowException() {
        IdeaRate ideaRate = IdeaRate.builder()
                .ideaRateId(IDEA_RATE_ID)
                .build();
        Mockito.when(ideaRateRepository.save(ideaRate))
                .thenThrow(ConstraintViolationException.class);
        assertThatThrownBy(
                () -> ideaRateService.add(ideaRate)
        ).isInstanceOf(ResourceConstraintViolationException.class);
    }

    @Test
    public void whenUpdate() {
        IdeaRate ideaRate = IdeaRate.builder()
                .ideaRateId(IDEA_RATE_ID)
                .build();
        Mockito.when(ideaRateRepository.findById(IDEA_RATE_ID)).thenReturn(Optional.of(ideaRate));
        Mockito.when(ideaRateRepository.save(ideaRate)).thenReturn(ideaRate);
        IdeaRate updatedIdeaRate = ideaRateService.update(IDEA_RATE_ID, ideaRate);
        Mockito.verify(ideaRateRepository).findById(IDEA_RATE_ID);
        Mockito.verify(ideaRateRepository).save(ideaRate);
        assertThat(ideaRate).isEqualTo(updatedIdeaRate);
    }

    @Test
    public void whenUpdateNonExistentThenThrowException() {
        IdeaRate ideaRate = IdeaRate.builder()
                .ideaRateId(IDEA_RATE_ID)
                .build();
        Mockito.when(ideaRateRepository.findById(IDEA_RATE_ID)).thenReturn(Optional.empty());
        assertThatThrownBy(
                () -> ideaRateService.update(IDEA_RATE_ID, ideaRate)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void whenUpdateViolatesDataIntegrityThenThrowException() {
        IdeaRate ideaRate = IdeaRate.builder()
                .ideaRateId(IDEA_RATE_ID)
                .build();
        Mockito.when(ideaRateRepository.findById(IDEA_RATE_ID)).thenReturn(Optional.of(ideaRate));
        Mockito.when(ideaRateRepository.save(ideaRate))
                .thenThrow(DataIntegrityViolationException.class);
        assertThatThrownBy(
                () -> ideaRateService.update(IDEA_RATE_ID, ideaRate)
        ).isInstanceOf(ResourceConstraintViolationException.class);
    }

    @Test
    public void whenUpdateViolatesConstraintsThenThrowException() {
        IdeaRate ideaRate = IdeaRate.builder()
                .ideaRateId(IDEA_RATE_ID)
                .build();
        Mockito.when(ideaRateRepository.findById(IDEA_RATE_ID)).thenReturn(Optional.of(ideaRate));
        Mockito.when(ideaRateRepository.save(ideaRate))
                .thenThrow(ConstraintViolationException.class);
        assertThatThrownBy(
                () -> ideaRateService.update(IDEA_RATE_ID, ideaRate)
        ).isInstanceOf(ResourceConstraintViolationException.class);
    }

    @Test
    public void whenDelete() {
        IdeaRate ideaRate = IdeaRate.builder()
                .ideaRateId(IDEA_RATE_ID)
                .build();
        Mockito.when(ideaRateRepository.findById(IDEA_RATE_ID)).thenReturn(Optional.of(ideaRate));
        ideaRateService.delete(IDEA_RATE_ID);
        Mockito.verify(ideaRateRepository).findById(IDEA_RATE_ID);
        Mockito.verify(ideaRateRepository).deleteById(IDEA_RATE_ID);
    }

    @Test
    public void whenDeleteNonExistentThenThrowException() {
        Mockito.when(ideaRateRepository.findById(IDEA_RATE_ID)).thenReturn(Optional.empty());
        assertThatThrownBy(
                () -> ideaRateService.delete(IDEA_RATE_ID)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void whenLike() {
        Mockito.when(ideaService.findById(IDEA_RATE_ID.getIdea().getId())).thenReturn(IDEA_TEMPLATE);
        IdeaRate ideaRate = IdeaRate.builder()
                .ideaRateId(IDEA_RATE_ID)
                .build();
        Mockito.when(ideaRateRepository.save(ideaRate)).thenReturn(ideaRate);
        Assertions.assertAll(
                () -> ideaRateService.like(
                        IDEA_RATE_ID.getIdea().getId(),
                        IDEA_RATE_ID.getUserUuid()
                )
        );
        Mockito.verify(ideaRateRepository).save(ideaRate);
    }

    @Test
    public void whenLikeViolatesConstraintsThenThrowException() {
        Mockito.when(ideaService.findById(IDEA_RATE_ID.getIdea().getId())).thenReturn(IDEA_TEMPLATE);
        IdeaRate ideaRate = IdeaRate.builder()
                .ideaRateId(IDEA_RATE_ID)
                .build();
        Mockito.when(ideaRateRepository.save(ideaRate))
                .thenThrow(ConstraintViolationException.class);
        assertThatThrownBy(
                () -> ideaRateService.like(
                        IDEA_RATE_ID.getIdea().getId(),
                        IDEA_RATE_ID.getUserUuid()
                )
        ).isInstanceOf(ResourceConstraintViolationException.class);
    }

    @Test
    public void whenLikeViolatesDataIntegrityThenThrowException() {
        Mockito.when(ideaService.findById(IDEA_RATE_ID.getIdea().getId())).thenReturn(IDEA_TEMPLATE);
        IdeaRate ideaRate = IdeaRate.builder()
                .ideaRateId(IDEA_RATE_ID)
                .build();
        Mockito.when(ideaRateRepository.save(ideaRate))
                .thenThrow(DataIntegrityViolationException.class);
        assertThatThrownBy(
                () -> ideaRateService.like(
                        IDEA_RATE_ID.getIdea().getId(),
                        IDEA_RATE_ID.getUserUuid()
                )
        ).isInstanceOf(ResourceConstraintViolationException.class);
    }

    @Test
    public void whenDislike() {
        Mockito.when(ideaService.findById(IDEA_RATE_ID.getIdea().getId())).thenReturn(IDEA_TEMPLATE);
        IdeaRate ideaRate = IdeaRate.builder()
                .ideaRateId(IDEA_RATE_ID)
                .build();
        Mockito.when(ideaRateRepository.save(ideaRate)).thenReturn(ideaRate);
        Assertions.assertAll(
                () -> ideaRateService.dislike(
                        IDEA_RATE_ID.getIdea().getId(),
                        IDEA_RATE_ID.getUserUuid()
                )
        );
        Mockito.verify(ideaRateRepository).save(ideaRate);
    }

    @Test
    public void whenDislikeViolatesConstraintsThenThrowException() {
        Mockito.when(ideaService.findById(IDEA_RATE_ID.getIdea().getId())).thenReturn(IDEA_TEMPLATE);
        IdeaRate ideaRate = IdeaRate.builder()
                .ideaRateId(IDEA_RATE_ID)
                .build();
        Mockito.when(ideaRateRepository.save(ideaRate))
                .thenThrow(ConstraintViolationException.class);
        assertThatThrownBy(
                () -> ideaRateService.dislike(
                        IDEA_RATE_ID.getIdea().getId(),
                        IDEA_RATE_ID.getUserUuid()
                )
        ).isInstanceOf(ResourceConstraintViolationException.class);
    }

    @Test
    public void whenDislikeViolatesDataIntegrityThenThrowException() {
        Mockito.when(ideaService.findById(IDEA_RATE_ID.getIdea().getId())).thenReturn(IDEA_TEMPLATE);
        IdeaRate ideaRate = IdeaRate.builder()
                .ideaRateId(IDEA_RATE_ID)
                .build();
        Mockito.when(ideaRateRepository.save(ideaRate))
                .thenThrow(DataIntegrityViolationException.class);
        assertThatThrownBy(
                () -> ideaRateService.dislike(
                        IDEA_RATE_ID.getIdea().getId(),
                        IDEA_RATE_ID.getUserUuid()
                )
        ).isInstanceOf(ResourceConstraintViolationException.class);
    }
}