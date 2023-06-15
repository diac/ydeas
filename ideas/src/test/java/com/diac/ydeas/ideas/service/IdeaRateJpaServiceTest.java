package com.diac.ydeas.ideas.service;

import com.diac.ydeas.domain.exception.ResourceConstraintViolationException;
import com.diac.ydeas.domain.exception.ResourceNotFoundException;
import com.diac.ydeas.domain.model.Idea;
import com.diac.ydeas.domain.model.IdeaRate;
import com.diac.ydeas.domain.model.IdeaRateId;
import com.diac.ydeas.ideas.repository.IdeaRateRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        IDEA_TEMPLATE = Idea.builder()
                .id(number)
                .title(value)
                .description(value)
                .authorUserId(number)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private static final IdeaRateId IDEA_RATE_ID = IdeaRateId.builder()
            .idea(IDEA_TEMPLATE)
            .userId(1)
            .build();

    @Autowired
    private IdeaRateService ideaRateService;

    @MockBean
    private IdeaRateRepository ideaRateRepository;

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
    public void whenFindAllByIdeaRateIdIdeaId() {
        int ideaId = IDEA_RATE_ID.getIdea().getId();
        List<IdeaRate> expectedIdeaRates = List.of(
                IdeaRate.builder()
                        .ideaRateId(IDEA_RATE_ID)
                        .build()
        );
        Mockito.when(ideaRateRepository.findAllByIdeaRateIdIdeaId(ideaId)).thenReturn(expectedIdeaRates);
        List<IdeaRate> ideaRates = ideaRateService.findAllByIdeaRateIdIdeaId(ideaId);
        assertThat(ideaRates).isEqualTo(expectedIdeaRates);
        Mockito.verify(ideaRateRepository).findAllByIdeaRateIdIdeaId(ideaId);
    }

    @Test
    public void whenFindAllByIdeaRateIdUserId() {
        int userId = IDEA_RATE_ID.getUserId();
        List<IdeaRate> expectedIdeaRates = List.of(
                IdeaRate.builder()
                        .ideaRateId(IDEA_RATE_ID)
                        .build()
        );
        Mockito.when(ideaRateRepository.findAllByIdeaRateIdUserId(userId)).thenReturn(expectedIdeaRates);
        List<IdeaRate> ideaRates = ideaRateService.findAllByIdeaRateIdUserId(userId);
        assertThat(ideaRates).isEqualTo(expectedIdeaRates);
        Mockito.verify(ideaRateRepository).findAllByIdeaRateIdUserId(userId);
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
}