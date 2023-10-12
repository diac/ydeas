package com.diac.ydeas.ideas.controller;

import com.diac.ydeas.domain.dto.IdeaInputDto;
import com.diac.ydeas.domain.model.Idea;
import com.diac.ydeas.ideas.service.IdeaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

/**
 * Контроллер, реализующий доступ к объектам модели Idea
 */
@RestController
@AllArgsConstructor
@RequestMapping("/idea")
public class IdeaController {

    /**
     * Сервис для работы с объектами Idea
     */
    private final IdeaService ideaService;

    /**
     * Получить все идеи
     *
     * @param pageable Объект Pageable
     * @return Страница с идеями
     */
    @GetMapping("")
    public Page<Idea> index(@PageableDefault Pageable pageable) {
        return ideaService.getPage(pageable);
    }

    /**
     * Получить идею по UD
     *
     * @param id Идентификатор идеи
     * @return Идея
     */
    @GetMapping("/{id}")
    public Idea getById(@PathVariable("id") int id) {
        return ideaService.findById(id);
    }

    /**
     * Получить страницу с идеями автора-принципала
     *
     * @param pageable  Объект pageable
     * @param principal Объект Principal
     * @return Страница с идеями
     */
    @GetMapping("/my_ideas")
    public Page<Idea> myIdeas(
            @PageableDefault Pageable pageable,
            Principal principal
    ) {
        return ideaService.getPage(
                UUID.fromString(principal.getName()),
                pageable
        );
    }

    /**
     * Добавить новую идею в систему
     *
     * @param ideaInputDto DTO с данными новой идеи
     * @param principal    Объект Principal
     * @return Созданная идея
     */
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Idea post(
            @RequestBody @Valid IdeaInputDto ideaInputDto,
            Principal principal
    ) {
        return ideaService.add(
                ideaInputDto,
                UUID.fromString(principal.getName())
        );
    }

    /**
     * Обновить данные идеи
     *
     * @param id           Идентификатор идеи
     * @param ideaInputDto DTO с обновленными данными идеи
     * @param principal    Объект Principal
     * @return Обновленная идея
     */
    @PutMapping("/{id}")
    public Idea put(
            @PathVariable("id") int id,
            @RequestBody @Valid IdeaInputDto ideaInputDto,
            Principal principal
    ) {
        return ideaService.update(
                id,
                ideaInputDto,
                UUID.fromString(principal.getName())
        );
    }

    /**
     * Удалить идею из системы
     *
     * @param id        Идентификатор идеи
     * @param principal Объект Principal
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id, Principal principal) {
        ideaService.delete(
                id,
                UUID.fromString(principal.getName())
        );
    }
}