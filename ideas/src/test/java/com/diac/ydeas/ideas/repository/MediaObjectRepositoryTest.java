package com.diac.ydeas.ideas.repository;

import com.diac.ydeas.domain.model.MediaObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.*;

public class MediaObjectRepositoryTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    private MediaObjectRepository mediaObjectRepository;

    @Test
    public void whenFindAll() {
        String value = "test";
        long longValue = 1;
        MediaObject mediaObject = mediaObjectRepository.save(
                MediaObject.builder()
                        .url(value)
                        .fileName(value)
                        .originalFileName(value)
                        .mediaType(value)
                        .contentLength(longValue)
                        .title(value)
                        .description(value)
                        .build()
        );
        assertThat(mediaObjectRepository.findAll()).contains(mediaObject);
    }

    @Test
    public void whenFindById() {
        String value = "test";
        long longValue = 1;
        MediaObject mediaObject = mediaObjectRepository.save(
                MediaObject.builder()
                        .url(value)
                        .fileName(value)
                        .originalFileName(value)
                        .mediaType(value)
                        .contentLength(longValue)
                        .title(value)
                        .description(value)
                        .build()
        );
        MediaObject mediaObjectInDb = mediaObjectRepository.findById(mediaObject.getId()).orElse(new MediaObject());
        assertThat(mediaObjectInDb).isEqualTo(mediaObject);
    }

    @Test
    public void whenAdd() {
        String value = "test";
        long longValue = 1;
        MediaObject mediaObject = mediaObjectRepository.save(
                MediaObject.builder()
                        .url(value)
                        .fileName(value)
                        .originalFileName(value)
                        .mediaType(value)
                        .contentLength(longValue)
                        .title(value)
                        .description(value)
                        .build()
        );
        assertThat(mediaObject.getUrl()).isEqualTo(value);
        assertThat(mediaObject.getFileName()).isEqualTo(value);
        assertThat(mediaObject.getMediaType()).isEqualTo(value);
        assertThat(mediaObject.getContentLength()).isEqualTo(longValue);
    }

    @Test
    public void whenUpdate() {
        String value = "test";
        String updatedValue = value + "_updated";
        long longValue = 1;
        long updatedLongValue = longValue + 100;
        MediaObject mediaObject = mediaObjectRepository.save(
                MediaObject.builder()
                        .url(value)
                        .fileName(value)
                        .originalFileName(value)
                        .mediaType(value)
                        .contentLength(longValue)
                        .title(value)
                        .description(value)
                        .build()
        );
        mediaObject.setUrl(updatedValue);
        mediaObject.setFileName(updatedValue);
        mediaObject.setMediaType(updatedValue);
        mediaObject.setContentLength(updatedLongValue);
        MediaObject updatedMediaObject = mediaObjectRepository.save(mediaObject);
        assertThat(mediaObject).isEqualTo(updatedMediaObject);
        assertThat(mediaObject.getUrl()).isEqualTo(updatedMediaObject.getUrl());
        assertThat(mediaObject.getFileName()).isEqualTo(updatedMediaObject.getFileName());
        assertThat(mediaObject.getMediaType()).isEqualTo(updatedMediaObject.getMediaType());
        assertThat(mediaObject.getContentLength()).isEqualTo(updatedMediaObject.getContentLength());
    }

    @Test
    public void whenDelete() {
        String value = "test";
        long longValue = 1;
        MediaObject mediaObject = mediaObjectRepository.save(
                MediaObject.builder()
                        .url(value)
                        .fileName(value)
                        .originalFileName(value)
                        .mediaType(value)
                        .contentLength(longValue)
                        .title(value)
                        .description(value)
                        .build()
        );
        mediaObjectRepository.delete(mediaObject);
        assertThat(mediaObjectRepository.findAll()).doesNotContain(mediaObject);
    }
}