package com.diac.ydeas.ideas.controller;

import com.diac.ydeas.domain.dto.IdeaInputDto;
import com.diac.ydeas.domain.model.Idea;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Контроллер, реализующий доступ к объектам модели Idea
 */
@Tag(name = "IdeaController", description = "Контроллер, реализующий доступ к объектам модели Idea")
public interface IdeaController {

    /**
     * Получить все идеи
     *
     * @param pageable Объект Pageable
     * @return Страница с идеями
     */
    @GetMapping("")
    @Operation(summary = "Получить все идеи")
    Page<Idea> index(@Parameter(description = "Объект Pageable") Pageable pageable);

    /**
     * Получить идею по ID
     *
     * @param id Идентификатор идеи
     * @return Идея
     */
    @GetMapping("/{id}")
    @Operation(summary = "Получить идею по ID")
    Idea getById(@Parameter(description = "Идентификатор идеи") int id);

    /**
     * Получить страницу с идеями автора-принципала
     *
     * @param pageable  Объект Pageable
     * @param principal Объект Principal
     * @return Страница с идеями
     */
    @GetMapping("/my_ideas")
    @Operation(summary = "Получить страницу с идеями автора-принципала")
    Page<Idea> myIdeas(
            @Parameter(description = "Объект Pageable") Pageable pageable,
            @Parameter(description = "Объект Principal") Principal principal
    );

    /**
     * Добавить новую идею в систему
     *
     * @param ideaInputDto DTO с данными новой идеи
     * @param principal    Объект Principal
     * @return Созданная идея
     */
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Добавить новую идею в систему")
    Idea post(
            @Parameter(description = "DTO с данными новой идеи") IdeaInputDto ideaInputDto,
            @Parameter(description = "Объект Principal") Principal principal
    );

    /**
     * Обновить данные идеи
     *
     * @param id           Идентификатор идеи
     * @param ideaInputDto DTO с обновленными данными идеи
     * @param principal    Объект Principal
     * @return Обновленная идея
     */
    @PutMapping("/{id}")
    @Operation(summary = "Обновить данные идеи")
    Idea put(
            @Parameter(description = "Идентификатор идеи") int id,
            @Parameter(description = "DTO с обновленными данными идеи") IdeaInputDto ideaInputDto,
            @Parameter(description = "Объект Principal") Principal principal
    );

    /**
     * Удалить идею из системы
     *
     * @param id        Идентификатор идеи
     * @param principal Объект Principal
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Идентификатор идеи")
    void delete(@Parameter(description = "Объект Principal") int id, Principal principal);
}