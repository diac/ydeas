package com.diac.ydeas.ideas.repository;

import com.diac.ydeas.domain.model.Idea;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class IdeaRepositoryTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    private IdeaRepository ideaRepository;

    @Test
    public void whenFindAll() {
        int number = 1;
        String value = String.valueOf(System.currentTimeMillis());
        Idea idea = ideaRepository.save(
                Idea.builder()
                        .title(value)
                        .description(value)
                        .authorUserId(number)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
        assertThat(ideaRepository.findAll()).contains(idea);
    }

    @Test
    public void whenFindById() {
        int number = 1;
        String value = String.valueOf(System.currentTimeMillis());
        Idea idea = ideaRepository.save(
                Idea.builder()
                .title(value)
                .description(value)
                .authorUserId(number)
                .createdAt(LocalDateTime.now())
                .build()
        );
        Idea ideaInDb = ideaRepository.findById(idea.getId())
                .orElse(new Idea());
        assertThat(ideaInDb).isEqualTo(idea);
    }

    @Test
    public void whenAdd() {
        int number = 1;
        String value = String.valueOf(System.currentTimeMillis());
        Idea idea = ideaRepository.save(
                Idea.builder()
                        .title(value)
                        .description(value)
                        .authorUserId(number)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
        assertThat(value).isEqualTo(idea.getTitle());
        assertThat(value).isEqualTo(idea.getDescription());
        assertThat(number).isEqualTo(idea.getAuthorUserId());
    }

    @Test
    public void whenUpdate() {
        int number = 1;
        String value = String.valueOf(System.currentTimeMillis());
        Idea idea = ideaRepository.save(
                Idea.builder()
                        .title(value)
                        .description(value)
                        .authorUserId(number)
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
        int number = 1;
        String value = String.valueOf(System.currentTimeMillis());
        Idea idea = ideaRepository.save(
                Idea.builder()
                        .title(value)
                        .description(value)
                        .authorUserId(number)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
        ideaRepository.delete(idea);
        assertThat(ideaRepository.findAll()).doesNotContain(idea);
    }
}