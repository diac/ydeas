package com.diac.ydeas.ideas.controller;

import com.diac.ydeas.domain.model.Idea;
import com.diac.ydeas.ideas.service.IdeaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер, реализующий доступ к объектам модели Idea
 */
@RestController
@AllArgsConstructor
@RequestMapping("/idea")
public class IdeaController {

    /**
     * Количество идей на странице
     */
    private static final int IDEAS_PER_PAGE = 10;

    /**
     * Сервис для работы с объектами Idea
     */
    private final IdeaService ideaService;

    /**
     * Получить все идеи
     *
     * @param pageNumber Номер страницы
     * @return Страница с идеями
     */
    @GetMapping("")
    public ResponseEntity<Page<Idea>> index(
            @RequestParam(name = "page", required = false, defaultValue = "1") int pageNumber
    ) {
        return new ResponseEntity<>(
                ideaService.getPage(PageRequest.of(pageNumber, IDEAS_PER_PAGE)),
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
     * Добавить новую идею в систему
     *
     * @param idea Новая идея
     * @return Ответ с созданной идеей
     */
    @PostMapping("")
    public ResponseEntity<Idea> post(@RequestBody @Valid Idea idea) {
        return new ResponseEntity<>(
                ideaService.add(idea),
                HttpStatus.CREATED
        );
    }

    /**
     * Обновить данные идеи
     *
     * @param id   Идентификатор идеи
     * @param idea Объект с обновленными данными идеи
     * @return Ответ с обновленной идеей
     */
    @PutMapping("/{id}")
    public ResponseEntity<Idea> put(
            @PathVariable("id") int id,
            @RequestBody @Valid Idea idea
    ) {
        return new ResponseEntity<>(
                ideaService.update(id, idea),
                HttpStatus.OK
        );
    }

    /**
     * Удалить идею из системы
     *
     * @param id Идентификатор идеи
     * @return Тело ответа со статусом
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        ideaService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}