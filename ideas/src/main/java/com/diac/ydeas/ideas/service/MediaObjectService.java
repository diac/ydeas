package com.diac.ydeas.ideas.service;

import com.diac.ydeas.domain.model.MediaObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * Сервис для работы с объектами модели MediaObject
 */
public interface MediaObjectService {

    /**
     * Найти медиа объект по ID
     *
     * @param id Идентификатор медиа объекта
     * @return Найденный медиа объект
     */
    MediaObject findById(int id);

    /**
     * Загрузить файл для идеи
     *
     * @param multipartFile Файл
     * @param ideaId        Идентификатор идеи
     * @param authorUuid    UUID автора идеи
     * @return Прикрепленный файл
     */
    MediaObject uploadFileForIdea(MultipartFile multipartFile, int ideaId, UUID authorUuid);

    /**
     * Прикрепить файл к идее
     *
     * @param mediaObjectId Идентификатор медиа объекта
     * @param ideaId        Идентификатор идеи
     * @param authorUuid    UUID автора идеи
     */
    void associateWithIdea(int mediaObjectId, int ideaId, UUID authorUuid);

    /**
     * Открепить файл от идеи
     *
     * @param mediaObjectId Идентификатор медиа объекта-документа
     * @param ideaId        Идентификатор идеи
     * @param authorUuid    UUID автора идеи
     */
    void dissociateWithIdea(int mediaObjectId, int ideaId, UUID authorUuid);
}