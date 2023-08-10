package com.diac.ydeas.ideas.service;

import com.diac.ydeas.domain.dto.IdeaInputDto;
import com.diac.ydeas.domain.dto.StoredObjectDetailsDto;
import com.diac.ydeas.domain.exception.ResourceNotFoundException;
import com.diac.ydeas.domain.exception.ResourceOwnershipViolationException;
import com.diac.ydeas.domain.model.Idea;
import com.diac.ydeas.domain.model.MediaObject;
import com.diac.ydeas.ideas.repository.MediaObjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * Сервис для работы с объектами модели MediaObject
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MediaObjectServiceImpl implements MediaObjectService {

    /**
     * Шаблон сообщения о том, что медиа объект не найден
     */
    private static final String MEDIA_TYPE_DOES_NOT_EXIST_MESSAGE = "Media type %s does not exist";

    /**
     * Шаблон сообщения о нарушении прав доступа к идее
     */
    private static final String IDEA_OWNERSHIP_VIOLATION_MESSAGE = "You don't have permission to modify this idea (#%s)";

    /**
     * Репозиторий для хранения объектов MediaObject
     */
    private final MediaObjectRepository mediaObjectRepository;

    /**
     * Сервис для работы с объектами Idea
     */
    private final IdeaService ideaService;

    /**
     * Бин RestTemplate
     */
    private final RestTemplate restTemplate;

    /**
     * URL сервиса для работы с файлами
     */
    @Value("${files-service-url}")
    private String fileServiceUrl;

    /**
     * Найти медиа объект по ID
     *
     * @param id Идентификатор медиа объекта
     * @return Найденный медиа объект
     */
    @Override
    public MediaObject findById(int id) {
        return mediaObjectRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                String.format(MEDIA_TYPE_DOES_NOT_EXIST_MESSAGE, id)
                        )
                );
    }

    /**
     * Загрузить файл для идеи
     *
     * @param multipartFile Файл
     * @param ideaId        Идентификатор идеи
     * @param authorUuid    UUID автора идеи
     * @return Прикрепленный файл
     * @throws ResourceOwnershipViolationException при нарушении прав доступа к ресурсу
     */
    @Override
    public MediaObject uploadFileForIdea(MultipartFile multipartFile, int ideaId, UUID authorUuid) {
        Idea idea = ideaService.findById(ideaId);
        if (!idea.getAuthorUuid().equals(authorUuid)) {
            throw new ResourceOwnershipViolationException(
                    String.format(IDEA_OWNERSHIP_VIOLATION_MESSAGE, ideaId)
            );
        }
        MediaObject mediaObject = upload(multipartFile);
        IdeaInputDto ideaInputDto = IdeaInputDto.builder()
                .title(idea.getTitle())
                .description(idea.getDescription())
                .attachments(idea.getAttachments())
                .build();
        ideaInputDto.getAttachments().add(mediaObject);
        ideaService.update(ideaId, ideaInputDto, authorUuid);
        return mediaObject;
    }

    /**
     * Прикрепить файл к идее
     *
     * @param mediaObjectId Идентификатор медиа объекта
     * @param ideaId        Идентификатор идеи
     * @param authorUuid    UUID автора идеи
     * @throws ResourceOwnershipViolationException при нарушении прав доступа к ресурсу
     */
    @Override
    public void associateWithIdea(int mediaObjectId, int ideaId, UUID authorUuid) {
        Idea idea = ideaService.findById(ideaId);
        if (!idea.getAuthorUuid().equals(authorUuid)) {
            throw new ResourceOwnershipViolationException(
                    String.format(IDEA_OWNERSHIP_VIOLATION_MESSAGE, ideaId)
            );
        }
        MediaObject mediaObject = findById(mediaObjectId);
        IdeaInputDto ideaInputDto = IdeaInputDto.builder()
                .title(idea.getTitle())
                .description(idea.getDescription())
                .attachments(idea.getAttachments())
                .build();
        ideaInputDto.getAttachments().add(mediaObject);
        ideaService.update(ideaId, ideaInputDto, authorUuid);
    }

    /**
     * Открепить документ от идеи
     *
     * @param mediaObjectId Идентификатор медиа объекта-документа
     * @param ideaId        Идентификатор идеи
     * @param authorUuid    UUID автора идеи
     */
    @Override
    public void dissociateWithIdea(int mediaObjectId, int ideaId, UUID authorUuid) {
        Idea idea = ideaService.findById(ideaId);
        if (!idea.getAuthorUuid().equals(authorUuid)) {
            throw new ResourceOwnershipViolationException(
                    String.format(IDEA_OWNERSHIP_VIOLATION_MESSAGE, ideaId)
            );
        }
        MediaObject mediaObject = findById(mediaObjectId);
        IdeaInputDto ideaInputDto = IdeaInputDto.builder()
                .title(idea.getTitle())
                .description(idea.getDescription())
                .attachments(idea.getAttachments())
                .build();
        ideaInputDto.getAttachments().remove(mediaObject);
        ideaService.update(ideaId, ideaInputDto, authorUuid);
    }

    /**
     * Загрузить медиа-объект
     *
     * @param multipartFile Загружаемый файл
     * @return Загруженный MediaObject
     */
    private MediaObject upload(MultipartFile multipartFile) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", multipartFile.getResource());
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<StoredObjectDetailsDto> responseEntity = restTemplate.postForEntity(
                fileServiceUrl,
                requestEntity,
                StoredObjectDetailsDto.class
        );
        StoredObjectDetailsDto storedObjectDetailsDto = responseEntity.getBody();
        MediaObject mediaObject = MediaObject.builder()
                .url(String.format("%s/%s", fileServiceUrl, storedObjectDetailsDto.objectKey()))
                .fileName(storedObjectDetailsDto.objectKey())
                .originalFileName(multipartFile.getOriginalFilename())
                .mediaType(storedObjectDetailsDto.mediaType())
                .contentLength(storedObjectDetailsDto.contentLength())
                .title(multipartFile.getOriginalFilename())
                .description(multipartFile.getOriginalFilename())
                .build();
        return mediaObjectRepository.save(mediaObject);
    }
}