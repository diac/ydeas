package com.diac.ydeas.ideas.service;

import com.diac.ydeas.domain.model.IdeaRating;
import com.diac.ydeas.ideas.repository.IdeaRatingRepository;
import lombok.AllArgsConstructor;
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
}