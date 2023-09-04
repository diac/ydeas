package com.diac.ydeas.ideas.service;

import com.diac.ydeas.domain.dto.IdeaInputDto;
import com.diac.ydeas.domain.model.Idea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы с объектами Idea
 */
public interface IdeaService {

    /**
     * Найти все идеи
     *
     * @return Список идей
     */
    List<Idea> findAll();

    /**
     * Получить страницу с идеями
     *
     * @param pageable Объект Pageable
     * @return Страница с идеями
     */
    Page<Idea> getPage(Pageable pageable);

    /**
     * Получить страницу с идеями по UUID автора
     *
     * @param authorUuid UUID автора идей
     * @param pageable Объект Pageable
     * @return Страница с идеями
     */
    Page<Idea> getPage(UUID authorUuid, Pageable pageable);

    /**
     * Найти идею по ID
     *
     * @param id Идентификатор идеи
     * @return Идея
     */
    Idea findById(int id);

    /**
     * Добавить новую идею в систему
     *
     * @param ideaInputDto DTO с данными новой идеи
     * @param authorUuid   UUID автора идеи
     * @return Сохраненная идея
     */
    Idea add(IdeaInputDto ideaInputDto, UUID authorUuid);

    /**
     * Обновить данные идеи в системе
     *
     * @param id           Идентификатор идеи, данные которой необходимо обновить
     * @param ideaInputDto DTO с обновленными данными идеи
     * @param authorUuid   UUID автора идеи
     * @return Обновленная идея
     */
    Idea update(int id, IdeaInputDto ideaInputDto, UUID authorUuid);

    /**
     * Удалить идею из системы
     *
     * @param id         Идентификатор идеи, которую необходимо удалить
     * @param authorUuid UUID автора идеи
     */
    void delete(int id, UUID authorUuid);
}