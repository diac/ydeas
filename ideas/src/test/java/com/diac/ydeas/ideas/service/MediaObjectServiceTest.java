package com.diac.ydeas.ideas.service;

import com.diac.ydeas.domain.dto.IdeaInputDto;
import com.diac.ydeas.domain.dto.StoredObjectDetailsDto;
import com.diac.ydeas.domain.exception.ResourceNotFoundException;
import com.diac.ydeas.domain.exception.ResourceOwnershipViolationException;
import com.diac.ydeas.domain.model.Idea;
import com.diac.ydeas.domain.model.MediaObject;
import com.diac.ydeas.ideas.repository.MediaObjectRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {
        MediaObjectServiceImpl.class
})
public class MediaObjectServiceTest {

    @Autowired
    private MediaObjectService mediaObjectService;

    @MockBean
    private MediaObjectRepository mediaObjectRepository;

    @MockBean
    private IdeaService ideaService;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void whenFindByIdFound() {
        int id = 1;
        MediaObject mediaObject = MediaObject.builder()
                .id(id)
                .build();
        Mockito.when(mediaObjectRepository.findById(id)).thenReturn(Optional.of(mediaObject));
        assertThat(mediaObjectService.findById(id)).isSameAs(mediaObject);
        Mockito.verify(mediaObjectRepository).findById(id);
    }

    @Test
    public void whenFindByIdNotFoundThenThrowException() {
        int id = 1;
        Mockito.when(mediaObjectRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(
                () -> mediaObjectService.findById(id)
        ).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void whenUploadFileForIdea() throws IllegalAccessException {
        String value = "test";
        int intValue = 1;
        long longValue = 1;
        UUID uuid = UUID.randomUUID();
        Idea idea = Idea.builder()
                .id(intValue)
                .title(value)
                .description(value)
                .authorUuid(uuid)
                .attachments(new HashSet<>())
                .build();
        Mockito.when(ideaService.findById(intValue)).thenReturn(idea);
        IdeaInputDto ideaInputDto = IdeaInputDto.builder()
                .title(idea.getTitle())
                .description(idea.getDescription())
                .attachments(idea.getAttachments())
                .build();
        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                value,
                MediaType.IMAGE_PNG_VALUE,
                value.getBytes()
        );
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", multipartFile.getResource());
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        StoredObjectDetailsDto storedObjectDetailsDto = new StoredObjectDetailsDto(
                value,
                value,
                value,
                intValue
        );
        ResponseEntity<StoredObjectDetailsDto> responseEntity = ResponseEntity.ok(storedObjectDetailsDto);
        Mockito.when(restTemplate.postForEntity(
                value,
                requestEntity,
                StoredObjectDetailsDto.class
        )).thenReturn(responseEntity);
        Field field = ReflectionUtils
                .findField(MediaObjectServiceImpl.class, "fileServiceUrl");
        field.setAccessible(true);
        field.set(mediaObjectService, value);
        MediaObject mediaObject = MediaObject.builder()
                .url(value)
                .fileName(value)
                .originalFileName(value)
                .mediaType(value)
                .contentLength(longValue)
                .title(value)
                .description(value)
                .build();
        MediaObject expectedMediaObject = MediaObject.builder()
                .id(intValue)
                .url(value)
                .fileName(value)
                .originalFileName(value)
                .mediaType(value)
                .contentLength(longValue)
                .title(value)
                .description(value)
                .build();
        Mockito.when(mediaObjectRepository.save(mediaObject)).thenReturn(expectedMediaObject);
        MediaObject uploadedImage = mediaObjectService.uploadFileForIdea(multipartFile, intValue, uuid);
        Mockito.verify(restTemplate).postForEntity(
                value,
                requestEntity,
                StoredObjectDetailsDto.class
        );
        Mockito.verify(ideaService).update(idea.getId(), ideaInputDto, uuid);
        assertThat(uploadedImage).isSameAs(expectedMediaObject);
    }

    @Test
    public void whenUploadFileForIdeaViolatesOwnershipThenThrowResourceOwnershipException() {
        String value = "test";
        int intValue = 1;
        UUID authorUuid = UUID.randomUUID();
        UUID strangerUuid = UUID.randomUUID();
        Idea idea = Idea.builder()
                .id(intValue)
                .title(value)
                .description(value)
                .authorUuid(authorUuid)
                .attachments(new HashSet<>())
                .build();
        Mockito.when(ideaService.findById(intValue)).thenReturn(idea);
        MultipartFile multipartFile = new MockMultipartFile(
                "file",
                value,
                MediaType.IMAGE_PNG_VALUE,
                value.getBytes()
        );
        assertThatThrownBy(
                () -> mediaObjectService.uploadFileForIdea(multipartFile, intValue, strangerUuid)
        ).isInstanceOf(ResourceOwnershipViolationException.class);
    }

    @Test
    public void whenAssociateWithIdea() {
        int id = 1;
        UUID uuid = UUID.randomUUID();
        Idea idea = Idea.builder()
                .id(id)
                .authorUuid(uuid)
                .attachments(new HashSet<>())
                .build();
        MediaObject mediaObject = MediaObject.builder()
                .id(id)
                .mediaType(MediaType.IMAGE_PNG_VALUE)
                .build();
        Mockito.when(ideaService.findById(id)).thenReturn(idea);
        Mockito.when(mediaObjectRepository.findById(id)).thenReturn(Optional.of(mediaObject));
        Assertions.assertAll(
                () -> mediaObjectService.associateWithIdea(id, id, uuid)
        );
    }

    @Test
    public void whenAssociateWithIdeaViolatesOwnershipThenThrowResourceOwnershipException() {
        int id = 1;
        UUID authorUuid = UUID.randomUUID();
        UUID strangerUUid = UUID.randomUUID();
        Idea idea = Idea.builder()
                .id(id)
                .authorUuid(authorUuid)
                .attachments(new HashSet<>())
                .build();
        MediaObject mediaObject = MediaObject.builder()
                .id(id)
                .mediaType(MediaType.IMAGE_PNG_VALUE)
                .build();
        Mockito.when(ideaService.findById(id)).thenReturn(idea);
        Mockito.when(mediaObjectRepository.findById(id)).thenReturn(Optional.of(mediaObject));
        assertThatThrownBy(
                () -> mediaObjectService.associateWithIdea(id, id, strangerUUid)
        ).isInstanceOf(ResourceOwnershipViolationException.class);
    }

    @Test
    public void whenDissociateWithIdea() {
        int id = 1;
        UUID uuid = UUID.randomUUID();
        Idea idea = Idea.builder()
                .id(id)
                .authorUuid(uuid)
                .attachments(new HashSet<>())
                .build();
        MediaObject mediaObject = MediaObject.builder()
                .id(id)
                .mediaType(MediaType.IMAGE_PNG_VALUE)
                .build();
        Mockito.when(ideaService.findById(id)).thenReturn(idea);
        Mockito.when(mediaObjectRepository.findById(id)).thenReturn(Optional.of(mediaObject));
        Assertions.assertAll(
                () -> mediaObjectService.dissociateWithIdea(id, id, uuid)
        );
        assertThat(idea.getAttachments()).doesNotContain(mediaObject);
    }

    @Test
    public void whenDissociateWithIdeaViolatesOwnershipThenThrowResourceOwnershipException() {
        int id = 1;
        UUID authorUuid = UUID.randomUUID();
        UUID strangerUUid = UUID.randomUUID();
        Idea idea = Idea.builder()
                .id(id)
                .authorUuid(authorUuid)
                .attachments(new HashSet<>())
                .build();
        MediaObject mediaObject = MediaObject.builder()
                .id(id)
                .mediaType(MediaType.IMAGE_PNG_VALUE)
                .build();
        Mockito.when(ideaService.findById(id)).thenReturn(idea);
        Mockito.when(mediaObjectRepository.findById(id)).thenReturn(Optional.of(mediaObject));
        assertThatThrownBy(
                () -> mediaObjectService.dissociateWithIdea(id, id, strangerUUid)
        ).isInstanceOf(ResourceOwnershipViolationException.class);
    }
}