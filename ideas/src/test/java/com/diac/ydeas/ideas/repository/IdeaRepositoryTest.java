package com.diac.ydeas.ideas.repository;

import com.diac.ydeas.domain.model.Idea;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class IdeaRepositoryTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    private IdeaRepository ideaRepository;

    @Test
    public void whenFindAll() {
        String value = String.valueOf(System.currentTimeMillis());
        UUID uuid = UUID.randomUUID();
        Idea idea = ideaRepository.save(
                Idea.builder()
                        .title(value)
                        .description(value)
                        .authorUuid(uuid)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
        assertThat(ideaRepository.findAll()).contains(idea);
    }

    @Test
    public void whenFindById() {
        UUID uuid = UUID.randomUUID();
        String value = String.valueOf(System.currentTimeMillis());
        Idea idea = ideaRepository.save(
                Idea.builder()
                .title(value)
                .description(value)
                .authorUuid(uuid)
                .createdAt(LocalDateTime.now())
                .build()
        );
        Idea ideaInDb = ideaRepository.findById(idea.getId())
                .orElse(new Idea());
        assertThat(ideaInDb).isEqualTo(idea);
    }

    @Test
    public void whenAdd() {
        UUID uuid = UUID.randomUUID();
        String value = String.valueOf(System.currentTimeMillis());
        Idea idea = ideaRepository.save(
                Idea.builder()
                        .title(value)
                        .description(value)
                        .authorUuid(uuid)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
        assertThat(value).isEqualTo(idea.getTitle());
        assertThat(value).isEqualTo(idea.getDescription());
        assertThat(uuid).isEqualTo(idea.getAuthorUuid());
    }

    @Test
    public void whenUpdate() {
        UUID uuid = UUID.randomUUID();
        String value = String.valueOf(System.currentTimeMillis());
        Idea idea = ideaRepository.save(
                Idea.builder()
                        .title(value)
                        .description(value)
                        .authorUuid(uuid)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
        idea.setTitle(idea.getTitle() + "_updated");
        idea.setDescription(idea.getDescription() + "_updated");
        Idea updatedIdea = ideaRepository.save(idea);
        assertThat(idea).isEqualTo(updatedIdea);
        assertThat(idea.getTitle()).isEqualTo(updatedIdea.getTitle());
        assertThat(idea.getDescription()).isEqualTo(updatedIdea.getDescription());
    }

    @Test
    public void whenDelete() {
        UUID uuid = UUID.randomUUID();
        String value = String.valueOf(System.currentTimeMillis());
        Idea idea = ideaRepository.save(
                Idea.builder()
                        .title(value)
                        .description(value)
                        .authorUuid(uuid)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
        ideaRepository.delete(idea);
        assertThat(ideaRepository.findAll()).doesNotContain(idea);
    }
}