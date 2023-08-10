package com.diac.ydeas.ideas.service;

import com.diac.ydeas.domain.model.MediaObject;
import org.springframework.web.multipart.MultipartFile;

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
     * @return Прикрепленный файл
     */
    MediaObject uploadFileForIdea(MultipartFile multipartFile, int ideaId);

    /**
     * Прикрепить файл к идее
     *
     * @param mediaObjectId Идентификатор медиа объекта
     * @param ideaId        Идентификатор идеи
     */
    void associateWithIdea(int mediaObjectId, int ideaId);

    /**
     * Открепить документ от идеи
     *
     * @param mediaObjectId Идентификатор медиа объекта-документа
     * @param ideaId     Идентификатор идеи
     */
    void dissociateWithIdea(int mediaObjectId, int ideaId);
}