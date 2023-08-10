package com.diac.ydeas.ideas.repository;

import com.diac.ydeas.domain.model.MediaObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для хранения объектов MediaObject (медиа-объект)
 */
@Repository
public interface MediaObjectRepository extends JpaRepository<MediaObject, Integer> {
}