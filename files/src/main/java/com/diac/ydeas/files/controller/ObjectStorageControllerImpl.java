package com.diac.ydeas.files.controller;

import com.diac.ydeas.domain.dto.FileDownloadResultDto;
import com.diac.ydeas.domain.dto.StoredObjectDetailsDto;
import com.diac.ydeas.files.service.ObjectStorageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Контроллер для работы с хранилищем объектов
 */
@RestController
@RequestMapping("")
@AllArgsConstructor
public class ObjectStorageControllerImpl implements ObjectStorageController {

    /**
     * Сервис для работы с хранилищем объектов
     */
    private final ObjectStorageService objectStorageService;

    /**
     * Загрузить объект в хранилище
     *
     * @param multipartFile загружаемый объект
     * @return Загруженный объект
     */
    @Override
    public StoredObjectDetailsDto upload(@RequestParam("file") MultipartFile multipartFile) {
        return objectStorageService.upload(multipartFile);
    }

    /**
     * Загрузить файл из хранилища
     *
     * @param fileName Имя файла
     * @return Тело ответа с ресурсом, содержащим скачиваемый файл
     */
    @Override
    public ResponseEntity<Resource> download(@PathVariable("fileName") String fileName) {
        FileDownloadResultDto fileDownloadResultDto = objectStorageService.download(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileDownloadResultDto.mediaType()))
                .body(fileDownloadResultDto.resource());
    }

    /**
     * Удалить файл из хранилища
     *
     * @param fileName Имя файла
     */
    @Override
    public void delete(@PathVariable("fileName") String fileName) {
        objectStorageService.delete(fileName);
    }
}