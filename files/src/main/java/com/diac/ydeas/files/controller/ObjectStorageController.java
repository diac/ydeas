package com.diac.ydeas.files.controller;

import com.diac.ydeas.domain.dto.StoredObjectDetailsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

/**
 * Контроллер для работы с хранилищем объектов
 */
@Tag(name = "ObjectStorageController", description = "Контроллер для работы с хранилищем объектов")
public interface ObjectStorageController {

    /**
     * Загрузить объект в хранилище
     *
     * @param multipartFile загружаемый объект
     * @return Загруженный объект
     */
    @PostMapping("")
    @Operation(summary = "Загрузить объект в хранилище")
    StoredObjectDetailsDto upload(@Parameter(description = "Загружаемый объект") MultipartFile multipartFile);

    /**
     * Загрузить файл из хранилища
     *
     * @param fileName Имя файла
     * @param response Объект HttpServletResponse
     * @return Ресурс -- скачиваемый файл
     */
    @GetMapping("/{fileName}")
    @Operation(summary = "Загрузить файл из хранилища")
    Resource download(
            @Parameter(description = "Имя файла") String fileName,
            HttpServletResponse response
    );

    /**
     * Удалить файл из хранилища
     *
     * @param fileName Имя файла
     */
    @DeleteMapping("/{fileName}")
    @Operation(summary = "Удалить файл из хранилища")
    void delete(@Parameter(description = "Имя файла") String fileName);
}