package com.diac.ydeas.ideas.service;

import com.diac.ydeas.domain.exception.ResourceConstraintViolationException;
import com.diac.ydeas.domain.exception.ResourceNotFoundException;
import com.diac.ydeas.domain.model.Idea;
import com.diac.ydeas.ideas.repository.IdeaRepository;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для работы с объектами Idea
 */
@Service
@AllArgsConstructor
public class IdeaJpaService implements IdeaService {

    private static final String IDEA_DOES_NOT_EXIST_MESSAGE = "Idea #%s does not exist";

    /**
     * Репозиторий для хранения объектов Idea
     */
    private final IdeaRepository ideaRepository;

    /**
     * Найти все идеи
     *
     * @return Список идей
     */
    @Override
    public List<Idea> findAll() {
        return ideaRepository.findAll();
    }

    /**
     * Получить страницу с идеями
     *
     * @param pageRequest Объект PageRequest
     * @return Страница с идеями
     */
    @Override
    public Page<Idea> getPage(PageRequest pageRequest) {
        return ideaRepository.findAll(pageRequest);
    }

    /**
     * Найти идею по ID
     *
     * @param id Идентификатор идеи
     * @return Идея
     * @throws ResourceNotFoundException Если ничего не найдено
     */
    @Override
    public Idea findById(int id) {
        return ideaRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(String.format(IDEA_DOES_NOT_EXIST_MESSAGE, id))
                );
    }

    /**
     * Добавить новую идею в систему
     *
     * @param idea Новая идея
     * @return Сохраненная идея
     * @throws ResourceConstraintViolationException в случае, если при обращении к ресурсу нарушаются наложенные на него ограничения
     */
    @Override
    public Idea add(Idea idea) {
        try {
            idea.setCreatedAt(LocalDateTime.now());
            return ideaRepository.save(idea);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new ResourceConstraintViolationException(e.getMessage());
        }
    }

    /**
     * Обновить данные идеи в системе
     *
     * @param id   Идентификатор идеи, данные которой необходимо обновить
     * @param idea Объект с обновленными данными идеи
     * @return Обновленная идея
     * @throws ResourceConstraintViolationException в случае, если при обращении к ресурсу нарушаются наложенные на него ограничения
     */
    @Override
    public Idea update(int id, Idea idea) {
        try {
            return ideaRepository.findById(id)
                    .map(ideaInDb -> {
                        idea.setId(id);
                        return ideaRepository.save(idea);
                    }).orElseThrow(
                            () -> new ResourceNotFoundException(String.format(IDEA_DOES_NOT_EXIST_MESSAGE, id))
                    );
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new ResourceConstraintViolationException(e.getMessage());
        }
    }

    /**
     * Удалить идею из системы
     *
     * @param id Идентификатор идеи, которую необходимо удалить
     * @throws ResourceNotFoundException При попытке удалить несуществующую идею
     */
    @Override
    public void delete(int id) {
        ideaRepository.findById(id)
                .ifPresentOrElse(
                        idea -> ideaRepository.deleteById(id),
                        () -> {
                            throw new ResourceNotFoundException(
                                    String.format(IDEA_DOES_NOT_EXIST_MESSAGE, id)
                            );
                        }
                );
    }
}