package com.diac.ydeas.ideas.service;

import com.diac.ydeas.domain.exception.ResourceConstraintViolationException;
import com.diac.ydeas.domain.exception.ResourceNotFoundException;
import com.diac.ydeas.domain.exception.ResourceOwnershipViolationException;
import com.diac.ydeas.domain.model.Idea;
import com.diac.ydeas.domain.dto.IdeaInputDto;
import com.diac.ydeas.ideas.repository.IdeaRepository;
import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для работы с объектами Idea
 */
@Service
@AllArgsConstructor
public class IdeaJpaService implements IdeaService {

    /**
     * Шаблон сообщения о том, что идея не найдена
     */
    private static final String IDEA_DOES_NOT_EXIST_MESSAGE = "Idea #%s does not exist";

    /**
     * Шаблон сообщения о нарушении прав доступа к идее
     */
    private static final String IDEA_OWNERSHIP_VIOLATION_MESSAGE = "You don't have permission to modify this idea (#%s)";

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
     * Получить страницу с идеями по UUID автора
     *
     * @param authorUuid UUID автора идей
     * @param pageRequest pageRequest Объект PageRequest
     * @return Страница с идеями
     */
    @Override
    public Page<Idea> getPage(UUID authorUuid, PageRequest pageRequest) {
        return ideaRepository.findByAuthorUuid(authorUuid, pageRequest);
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
     * @param ideaInputDto DTO с данными новой идеи
     * @param authorUuid   UUID автора идеи
     * @return Сохраненная идея
     * @throws ResourceConstraintViolationException в случае, если при обращении к ресурсу нарушаются наложенные на него ограничения
     */
    @Override
    public Idea add(IdeaInputDto ideaInputDto, UUID authorUuid) {
        try {
            Idea idea = Idea.builder()
                    .title(ideaInputDto.getTitle())
                    .description(ideaInputDto.getDescription())
                    .createdAt(LocalDateTime.now())
                    .attachments(new HashSet<>())
                    .authorUuid(authorUuid)
                    .build();
            return ideaRepository.save(idea);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new ResourceConstraintViolationException(e.getMessage());
        }
    }

    /**
     * Обновить данные идеи в системе
     *
     * @param id           Идентификатор идеи, данные которой необходимо обновить
     * @param ideaInputDto DTO с обновленными данными идеи
     * @param authorUuid   UUID автора идеи
     * @return Обновленная идея
     * @throws ResourceConstraintViolationException в случае, если при обращении к ресурсу нарушаются наложенные на него ограничения
     * @throws ResourceOwnershipViolationException  при нарушении прав доступа к ресурсу
     */
    @Override
    public Idea update(int id, IdeaInputDto ideaInputDto, UUID authorUuid) {
        try {
            Optional<Idea> ideaOptional = ideaRepository.findById(id);
            if (ideaOptional.isEmpty()) {
                throw new ResourceNotFoundException(String.format(IDEA_DOES_NOT_EXIST_MESSAGE, id));
            }
            Idea idea = ideaOptional.get();
            if (!idea.getAuthorUuid().equals(authorUuid)) {
                throw new ResourceOwnershipViolationException(
                        String.format(IDEA_OWNERSHIP_VIOLATION_MESSAGE, id)
                );
            }
            idea.setTitle(ideaInputDto.getTitle());
            idea.setDescription(ideaInputDto.getDescription());
            idea.setAttachments(ideaInputDto.getAttachments());
            return ideaRepository.save(idea);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            throw new ResourceConstraintViolationException(e.getMessage());
        }
    }

    /**
     * Удалить идею из системы
     *
     * @param id         Идентификатор идеи, которую необходимо удалить
     * @param authorUuid UUID автора идеи
     * @throws ResourceNotFoundException           При попытке удалить несуществующую идею
     * @throws ResourceOwnershipViolationException при нарушении прав доступа к ресурсу
     */
    @Override
    public void delete(int id, UUID authorUuid) {
        Optional<Idea> ideaOptional = ideaRepository.findById(id);
        if (ideaOptional.isEmpty()) {
            throw new ResourceNotFoundException(String.format(IDEA_DOES_NOT_EXIST_MESSAGE, id));
        }
        Idea idea = ideaOptional.get();
        if (!idea.getAuthorUuid().equals(authorUuid)) {
            throw new ResourceOwnershipViolationException(
                    String.format(IDEA_OWNERSHIP_VIOLATION_MESSAGE, id)
            );
        }
        ideaRepository.deleteById(id);
    }
}