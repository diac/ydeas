package com.diac.ydeas.ideas.controller;

import com.diac.ydeas.domain.model.MediaObject;
import com.diac.ydeas.ideas.service.MediaObjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.security.Principal;
import java.util.UUID;

/**
 * Контроллер, реализующий операции с объектами модели MediaObject
 */
@RestController
@AllArgsConstructor
@RequestMapping("/media")
public class MediaObjectController {

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
     * @return Тело ответа с прикрепленным медиа объектом
     */
    @PostMapping("/upload_for_idea")
    public ResponseEntity<MediaObject> uploadForIdea(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam("ideaId") int ideaId,
            Principal principal
    ) {
        MediaObject mediaObject = mediaObjectService.uploadFileForIdea(
                multipartFile,
                ideaId,
                UUID.fromString(principal.getName())
        );
        return ResponseEntity.created(URI.create(mediaObject.getUrl()))
                .body(mediaObject);
    }

    /**
     * Прикрепить медиа объект к идее
     *
     * @param mediaObjectId Идентификатор медиа объекта
     * @param ideaId        Идентификатор идеи
     * @param principal     Объект Principal
     * @return Тело ответа со статусом
     */
    @PostMapping("/idea_attachment")
    public ResponseEntity<Void> associateWithIdea(
            @RequestParam("mediaObjectId") int mediaObjectId,
            @RequestParam("ideaId") int ideaId,
            Principal principal
    ) {
        mediaObjectService.associateWithIdea(
                mediaObjectId,
                ideaId,
                UUID.fromString(principal.getName())
        );
        return ResponseEntity.ok().build();
    }

    /**
     * Открепить медиа объект от идеи
     *
     * @param mediaObjectId Идентификатор медиа объекта
     * @param ideaId        Идентификатор идеи
     * @param principal     Объект Principal
     * @return Тело ответа со статусом
     */
    @DeleteMapping("/idea_attachment")
    public ResponseEntity<Void> dissociateWithIdea(
            @RequestParam("mediaObjectId") int mediaObjectId,
            @RequestParam("ideaId") int ideaId,
            Principal principal
    ) {
        mediaObjectService.dissociateWithIdea(
                mediaObjectId,
                ideaId,
                UUID.fromString(principal.getName())
        );
        return ResponseEntity.ok().build();
    }
}