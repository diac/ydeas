package com.diac.ydeas.files.controller;

import com.diac.ydeas.domain.dto.FileDownloadResultDto;
import com.diac.ydeas.domain.dto.StoredObjectDetailsDto;
import com.diac.ydeas.files.service.ObjectStorageService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Контроллер для работы с хранилищем объектов
 */
@RestController
@RequestMapping("")
@AllArgsConstructor
public class ObjectStorageController {

    /**
     * Сервис для работы с хранилищем объектов
     */
    private final ObjectStorageService objectStorageService;

    /**
     * Загрузить объект в хранилище
     *
     * @param multipartFile загружаемый объект
     */
    @PostMapping("")
    public ResponseEntity<StoredObjectDetailsDto> upload(@RequestParam("file") MultipartFile multipartFile) {
        return ResponseEntity.ok()
                .body(objectStorageService.upload(multipartFile));
    }

    /**
     * Загрузить файл из хранилища
     *
     * @param fileName Имя файла
     * @return Тело ответа с ресурсом, содержащим скачиваемый файл
     */
    @GetMapping("/{fileName}")
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
     * @return Тело ответа со статусом
     */
    @DeleteMapping("/{fileName}")
    public ResponseEntity<Void> delete(@PathVariable("fileName") String fileName) {
        objectStorageService.delete(fileName);
        return ResponseEntity.ok().build();
    }
}