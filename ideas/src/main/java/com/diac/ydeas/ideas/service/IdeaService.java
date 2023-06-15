package com.diac.ydeas.ideas.service;

import com.diac.ydeas.domain.model.Idea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

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
     * @param pageRequest Объект PageRequest
     * @return Страница с идеями
     */
    Page<Idea> getPage(PageRequest pageRequest);

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
     * @param idea Новая идея
     * @return Сохраненная идея
     */
    Idea add(Idea idea);

    /**
     * Обновить данные идеи в системе
     *
     * @param id   Идентификатор идеи, данные которой необходимо обновить
     * @param idea Объект с обновленными данными идеи
     * @return Обновленная идея
     */
    Idea update(int id, Idea idea);

    /**
     * Удалить идею из системы
     *
     * @param id Идентификатор идеи, которую необходимо удалить
     */
    void delete(int id);
}