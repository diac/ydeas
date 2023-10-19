package com.diac.ydeas.ideas.controller;

import com.diac.ydeas.domain.dto.IdeaMediaObjectAssociationDto;
import com.diac.ydeas.domain.model.MediaObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

/**
 * Контроллер, реализующий операции с объектами модели MediaObject
 */
@Tag(name = "MediaObjectController", description = "Контроллер, реализующий операции с объектами модели MediaObject")
public interface MediaObjectController {

    /**
     * Загрузить файл для идеи
     *
     * @param multipartFile Файл
     * @param ideaId        Идентификатор идеи
     * @param principal     Объект Principal
     * @param response Объект HttpServletResponse
     * @return Прикрепленный медиа-объект
     */
    @PostMapping("/upload_for_idea/{ideaId}")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Загрузить файл для идеи")
    MediaObject uploadForIdea(
            @Parameter(description = "Файл") MultipartFile multipartFile,
            @Parameter(description = "Идентификатор идеи") int ideaId,
            @Parameter(description = "Объект Principal") Principal principal,
            HttpServletResponse response
    );

    /**
     * Прикрепить медиа объект к идее
     *
     * @param ideaMediaObjectAssociationDto DTO для определения связи между идеей и медиа-объектом
     * @param principal                     Объект Principal
     */
    @PostMapping(value = "/idea_attachment", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Прикрепить медиа объект к идее")
    void associateWithIdea(
            @Parameter(description = "DTO для определения связи между идеей и медиа-объектом") IdeaMediaObjectAssociationDto ideaMediaObjectAssociationDto,
            @Parameter(description = "Объект Principal") Principal principal
    );

    /**
     * Открепить медиа объект от идеи
     *
     * @param ideaMediaObjectAssociationDto DTO для определения связи между идеей и медиа-объектом
     * @param principal                     Объект Principal
     */
    @DeleteMapping("/idea_attachment")
    @Operation(summary = "Открепить медиа объект от идеи")
    void dissociateWithIdea(
            @Parameter(description = "DTO для определения связи между идеей и медиа-объектом") IdeaMediaObjectAssociationDto ideaMediaObjectAssociationDto,
            @Parameter(description = "Объект Principal") Principal principal
    );
}