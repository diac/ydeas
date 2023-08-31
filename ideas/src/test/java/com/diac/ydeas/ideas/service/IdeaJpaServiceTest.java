package com.diac.ydeas.ideas.service;

import com.diac.ydeas.domain.dto.IdeaInputDto;
import com.diac.ydeas.domain.exception.ResourceConstraintViolationException;
import com.diac.ydeas.domain.exception.ResourceNotFoundException;
import com.diac.ydeas.domain.exception.ResourceOwnershipViolationException;
import com.diac.ydeas.domain.model.Idea;
import com.diac.ydeas.ideas.repository.IdeaRepository;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {
        IdeaJpaService.class
})
public class IdeaJpaServiceTest {

    @Autowired
    private IdeaService ideaService;

    @MockBean
    private IdeaRepository ideaRepository;

    @Test
    public void whenFindAll() {
        List<Idea> expectedIdeas = List.of(
                Idea.builder()
                        .id(1)
                        .build(),
                Idea.builder()
                        .id(2)
                        .build()
        );
        Mockito.when(ideaRepository.findAll()).thenReturn(expectedIdeas);
        List<Idea> ideas = ideaService.findAll();
        Mockito.verify(ideaRepository).findAll();
        assertThat(ideas).isEqualTo(expectedIdeas);
    }

    @Test
    public void whenGetPage() {
        List<Idea> expectedIdeas = List.of(
                Idea.builder()
                        .id(1)
                        .build(),
                Idea.builder()
                        .id(2)
                        .build()
        );
        Page<Idea> expectedIdeaPage = Mockito.mock(Page.class);
        Mockito.when(expectedIdeaPage.getContent()).thenReturn(expectedIdeas);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Mockito.when(ideaRepository.findAll(pageRequest)).thenReturn(expectedIdeaPage);
        assertThat(ideaService.getPage(pageRequest).getContent()).isEqualTo(expectedIdeas);
        Mockito.verify(ideaRepository).findAll(pageRequest);
    }

    @Test
    public void whenGetPageByAuthorUuid() {
        UUID uuid = UUID.randomUUID();
        List<Idea> expectedIdeas = List.of(
                Idea.builder()
                        .id(1)
                        .authorUuid(uuid)
                        .build(),
                Idea.builder()
                        .id(2)
                        .authorUuid(uuid)
                        .build()
        );
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Idea> expectedIdeaPage = Mockito.mock(Page.class);
        Mockito.when(expectedIdeaPage.getContent()).thenReturn(expectedIdeas);
        Mockito.when(ideaRepository.findByAuthorUuid(uuid, pageRequest))
                .thenReturn(expectedIdeaPage);
        assertThat(ideaService.getPage(uuid, pageRequest).getContent()).containsAll(expectedIdeas);
        Mockito.verify(ideaRepository).findByAuthorUuid(uuid, pageRequest);
    }

    @Test
    public void whenFindByIdFound() {
        int id = 1;
        Idea idea = Idea.builder()
                .id(id)
                .build();
        Mockito.when(ideaRepository.findById(id)).thenReturn(Optional.of(idea));
        assertThat(ideaService.findById(id)).isEqualTo(idea);
        Mockito.verify(ideaRepository).findById(id);
    }

    @Test
    public void whenFindByIdNotFoundThenThrowException() {
        int id = 1;
        Mockito.when(ideaRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(
                () -> ideaService.findById(id)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void whenAdd() {
        int id = 1;
        UUID uuid = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        IdeaInputDto ideaInputDto = new IdeaInputDto();
        Idea newIdea = Idea.builder()
                .authorUuid(uuid)
                .createdAt(now)
                .build();
        Idea expectedIdea = Idea.builder()
                .id(id)
                .authorUuid(uuid)
                .createdAt(now)
                .build();
        Mockito.when(ideaRepository.save(newIdea)).thenReturn(expectedIdea);
        Idea ideaInDb = ideaService.add(ideaInputDto, uuid);
        Mockito.verify(ideaRepository).save(newIdea);
        assertThat(ideaInDb).isEqualTo(expectedIdea);
    }

    @Test
    public void whenAddViolatesConstraintsThenThrowException() {
        UUID uuid = UUID.randomUUID();
        Idea idea = Idea.builder()
                .build();
        IdeaInputDto ideaInputDto = new IdeaInputDto();
        Mockito.when(ideaRepository.save(idea))
                .thenThrow(ConstraintViolationException.class);
        assertThatThrownBy(
                () -> ideaService.add(ideaInputDto, uuid)
        ).isInstanceOf(ResourceConstraintViolationException.class);
    }

    @Test
    public void whenUpdate() {
        int id = 1;
        UUID uuid = UUID.randomUUID();
        Idea idea = Idea.builder()
                .id(id)
                .authorUuid(uuid)
                .build();
        IdeaInputDto ideaInputDto = new IdeaInputDto();
        Mockito.when(ideaRepository.findById(id)).thenReturn(Optional.of(idea));
        Mockito.when(ideaRepository.save(idea)).thenReturn(idea);
        Idea updatedIdea = ideaService.update(id, ideaInputDto, uuid);
        Mockito.verify(ideaRepository).findById(id);
        Mockito.verify(ideaRepository).save(idea);
        assertThat(idea).isEqualTo(updatedIdea);
    }

    @Test
    public void whenUpdateNonExistentThenThrowException() {
        int id = 1;
        UUID uuid = UUID.randomUUID();
        IdeaInputDto ideaInputDto = new IdeaInputDto();
        Mockito.when(ideaRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(
                () -> ideaService.update(id, ideaInputDto, uuid)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void whenUpdateViolatesDataIntegrityThenThrowException() {
        int id = 1;
        UUID uuid = UUID.randomUUID();
        Idea idea = Idea.builder()
                .id(id)
                .authorUuid(uuid)
                .build();
        IdeaInputDto ideaInputDto = new IdeaInputDto();
        Mockito.when(ideaRepository.findById(id)).thenReturn(Optional.of(idea));
        Mockito.when(ideaRepository.save(idea))
                .thenThrow(DataIntegrityViolationException.class);
        assertThatThrownBy(
                () -> ideaService.update(id, ideaInputDto, uuid)
        ).isInstanceOf(ResourceConstraintViolationException.class);
    }

    @Test
    public void whenUpdateViolatesConstraintsThenThrowException() {
        int id = 1;
        UUID uuid = UUID.randomUUID();
        Idea idea = Idea.builder()
                .id(id)
                .authorUuid(uuid)
                .build();
        IdeaInputDto ideaInputDto = new IdeaInputDto();
        Mockito.when(ideaRepository.findById(id)).thenReturn(Optional.of(idea));
        Mockito.when(ideaRepository.save(idea))
                .thenThrow(ConstraintViolationException.class);
        assertThatThrownBy(
                () -> ideaService.update(id, ideaInputDto, uuid)
        ).isInstanceOf(ResourceConstraintViolationException.class);
    }

    @Test
    public void whenUpdateViolatesOwnershipThenThrowResourceOwnershipViolationException() {
        int id = 1;
        UUID authorUuid = UUID.randomUUID();
        UUID strangerUuid = UUID.randomUUID();
        Idea idea = Idea.builder()
                .id(id)
                .authorUuid(authorUuid)
                .build();
        IdeaInputDto ideaInputDto = new IdeaInputDto();
        Mockito.when(ideaRepository.findById(id)).thenReturn(Optional.of(idea));
        assertThatThrownBy(
                () -> ideaService.update(id, ideaInputDto, strangerUuid)
        ).isInstanceOf(ResourceOwnershipViolationException.class);
    }

    @Test
    public void whenDelete() {
        int id = 1;
        UUID uuid = UUID.randomUUID();
        Idea idea = Idea.builder()
                .id(id)
                .authorUuid(uuid)
                .build();
        Mockito.when(ideaRepository.findById(id)).thenReturn(Optional.of(idea));
        ideaService.delete(id, uuid);
        Mockito.verify(ideaRepository).findById(id);
        Mockito.verify(ideaRepository).deleteById(id);
    }

    @Test
    public void whenDeleteNonExistentThenThrowException() {
        int id = 1;
        UUID uuid = UUID.randomUUID();
        Mockito.when(ideaRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(
                () -> ideaService.delete(id, uuid)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void whenDeleteViolatesOwnershipThenThrowResourceOwnershipViolationException() {
        int id = 1;
        UUID authorUuid = UUID.randomUUID();
        UUID strangerUuid = UUID.randomUUID();
        Idea idea = Idea.builder()
                .id(id)
                .authorUuid(authorUuid)
                .build();
        Mockito.when(ideaRepository.findById(id)).thenReturn(Optional.of(idea));
        assertThatThrownBy(
                () -> ideaService.delete(id, strangerUuid)
        ).isInstanceOf(ResourceOwnershipViolationException.class);
    }
}