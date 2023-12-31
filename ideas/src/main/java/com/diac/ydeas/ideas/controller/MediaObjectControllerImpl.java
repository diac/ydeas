package com.diac.ydeas.ideas.controller;

import com.diac.ydeas.domain.dto.IdeaMediaObjectAssociationDto;
import com.diac.ydeas.domain.model.MediaObject;
import com.diac.ydeas.ideas.service.MediaObjectService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.UUID;

/**
 * Контроллер, реализующий операции с объектами модели MediaObject
 */
@RestController
@AllArgsConstructor
@RequestMapping("/media")
public class MediaObjectControllerImpl implements MediaObjectController {

    /**
     * Сервис для работы с объектами модели ProductImage
     */
    private final MediaObjectService mediaObjectService;

    /**
     * Загрузить файл для идеи
     *
     * @param multipartFile Файл
     * @param ideaId        Идентификатор идеи
     * @param principal     Объект Principal
     * @param response      Объект HttpServletResponse
     * @return Прикрепленный медиа-объект
     */
    @Override
    public MediaObject uploadForIdea(
            @RequestParam("file") MultipartFile multipartFile,
            @PathVariable("ideaId") int ideaId,
            Principal principal,
            HttpServletResponse response
    ) {
        MediaObject mediaObject = mediaObjectService.uploadFileForIdea(
                multipartFile,
                ideaId,
                UUID.fromString(principal.getName())
        );
        response.setHeader(HttpHeaders.LOCATION, mediaObject.getUrl());
        return mediaObject;
    }

    /**
     * Прикрепить медиа объект к идее
     *
     * @param ideaMediaObjectAssociationDto DTO для определения связи между идеей и медиа-объектом
     * @param principal                     Объект Principal
     */
    @Override
    public void associateWithIdea(
            @RequestBody IdeaMediaObjectAssociationDto ideaMediaObjectAssociationDto,
            Principal principal
    ) {
        mediaObjectService.associateWithIdea(
                ideaMediaObjectAssociationDto.mediaObjectId(),
                ideaMediaObjectAssociationDto.ideaId(),
                UUID.fromString(principal.getName())
        );
    }

    /**
     * Открепить медиа объект от идеи
     *
     * @param ideaMediaObjectAssociationDto DTO для определения связи между идеей и медиа-объектом
     * @param principal                     Объект Principal
     */
    @Override
    public void dissociateWithIdea(
            @RequestBody IdeaMediaObjectAssociationDto ideaMediaObjectAssociationDto,
            Principal principal
    ) {
        mediaObjectService.dissociateWithIdea(
                ideaMediaObjectAssociationDto.mediaObjectId(),
                ideaMediaObjectAssociationDto.ideaId(),
                UUID.fromString(principal.getName())
        );
    }
}