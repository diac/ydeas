package com.diac.ydeas.files.service;

import com.diac.ydeas.domain.dto.FileDownloadResultDto;
import com.diac.ydeas.domain.dto.StoredObjectDetailsDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * Сервис для работы с хранилищем объектов
 */
public interface ObjectStorageService {

    /**
     * Загрузить объект в хранилище
     *
     * @param multipartFile загружаемый объект
     * @return DTO с деталями хранимого объекта
     */
    StoredObjectDetailsDto upload(MultipartFile multipartFile);

    /**
     * Загрузить файл из хранилища
     *
     * @param fileName Имя файла
     * @return Объект FileDownloadResultDto с результатом загрузки файла
     */
    FileDownloadResultDto download(String fileName);

    /**
     * Удалить файл из хранилища
     *
     * @param fileName Имя файла
     */
    void delete(String fileName);
}