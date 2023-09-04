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
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Page<Idea>> index(@PageableDefault Pageable pageable) {
        return new ResponseEntity<>(
                ideaService.getPage(pageable),
                HttpStatus.OK
        );
    }

    /**
     * Получить идею по UD
     *
     * @param id Идентификатор идеи
     * @return Ответ с идеей и статусом OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<Idea> getById(@PathVariable("id") int id) {
        return new ResponseEntity<>(
                ideaService.findById(id),
                HttpStatus.OK
        );
    }

    /**
     * Получить страницу с идеями автора-принципала
     *
     * @param pageable  Объект pageable
     * @param principal Объект Principal
     * @return Страница с идеями
     */
    @GetMapping("/my_ideas")
    public ResponseEntity<Page<Idea>> myIdeas(
            @PageableDefault Pageable pageable,
            Principal principal
    ) {
        return new ResponseEntity<>(
                ideaService.getPage(
                        UUID.fromString(principal.getName()),
                        pageable
                ),
                HttpStatus.OK
        );
    }

    /**
     * Добавить новую идею в систему
     *
     * @param ideaInputDto DTO с данными новой идеи
     * @param principal    Объект Principal
     * @return Ответ с созданной идеей
     */
    @PostMapping("")
    public ResponseEntity<Idea> post(
            @RequestBody @Valid IdeaInputDto ideaInputDto,
            Principal principal
    ) {
        return new ResponseEntity<>(
                ideaService.add(
                        ideaInputDto,
                        UUID.fromString(principal.getName())
                ),
                HttpStatus.CREATED
        );
    }

    /**
     * Обновить данные идеи
     *
     * @param id           Идентификатор идеи
     * @param ideaInputDto DTO с обновленными данными идеи
     * @param principal    Объект Principal
     * @return Ответ с обновленной идеей
     */
    @PutMapping("/{id}")
    public ResponseEntity<Idea> put(
            @PathVariable("id") int id,
            @RequestBody @Valid IdeaInputDto ideaInputDto,
            Principal principal
    ) {
        return new ResponseEntity<>(
                ideaService.update(
                        id,
                        ideaInputDto,
                        UUID.fromString(principal.getName())
                ),
                HttpStatus.OK
        );
    }

    /**
     * Удалить идею из системы
     *
     * @param id        Идентификатор идеи
     * @param principal Объект Principal
     * @return Тело ответа со статусом
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id, Principal principal) {
        ideaService.delete(
                id,
                UUID.fromString(principal.getName())
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }
}