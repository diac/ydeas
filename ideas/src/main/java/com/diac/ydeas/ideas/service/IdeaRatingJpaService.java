package com.diac.ydeas.ideas.service;

import com.diac.ydeas.domain.model.IdeaRating;
import com.diac.ydeas.ideas.repository.IdeaRatingRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с объектами IdeaRating
 */
@Service
@AllArgsConstructor
public class IdeaRatingJpaService implements IdeaRatingService {

    /**
     * Репозиторий для хранения объектов IdeaRating (Рейтинг идей)
     */
    private final IdeaRatingRepository ideaRatingRepository;

    /**
     * Получить рейтинговый список идей
     *
     * @return Рейтинговый список идей
     */
    @Override
    public List<IdeaRating> rating() {
        return ideaRatingRepository.findAll();
    }

    /**
     * Получить страницу рейтингового списка идей
     *
     * @param pageRequest Объект PageRequest
     * @return Страница с идеями
     */
    @Override
    public Page<IdeaRating> rating(PageRequest pageRequest) {
        return ideaRatingRepository.findAllByOrderByRatingDesc(pageRequest);
    }
}