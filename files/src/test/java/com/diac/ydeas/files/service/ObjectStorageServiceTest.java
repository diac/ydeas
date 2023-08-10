package com.diac.ydeas.files.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.diac.ydeas.domain.dto.FileDownloadResultDto;
import com.diac.ydeas.domain.dto.StoredObjectDetailsDto;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = {
        ObjectStorageServiceImpl.class
})
public class ObjectStorageServiceTest {

    @Autowired
    private ObjectStorageService objectStorageService;

    @MockBean
    private AmazonS3 amazonS3;

    @Test
    public void whenUpload() throws IllegalAccessException {
        String value = "test";
        Field s3BucketNameField = ReflectionUtils
                .findField(ObjectStorageServiceImpl.class, "s3BucketName");
        s3BucketNameField.setAccessible(true);
        s3BucketNameField.set(objectStorageService, value);
        Field targetFileNameFunctionField = ReflectionUtils
                .findField(ObjectStorageServiceImpl.class, "targetFileNameFunction");
        targetFileNameFunctionField.setAccessible(true);
        targetFileNameFunctionField.set(objectStorageService, (Function) (filename) -> filename);
        MultipartFile multipartFile = new MockMultipartFile(value, value, value, value.getBytes());
        StoredObjectDetailsDto expectedStoredObjectDetailsDto = new StoredObjectDetailsDto(
                value,
                value,
                value,
                value.length()
        );
        StoredObjectDetailsDto storedObjectDetailsDto = objectStorageService.upload(multipartFile);
        Mockito.verify(amazonS3).putObject(Mockito.any());
        assertThat(storedObjectDetailsDto).isEqualTo(expectedStoredObjectDetailsDto);
    }

    @Test
    public void whenUploadThrowsAmazonServiceExceptionThenThrowResponseStatusException() {
        String value = "test";
        MultipartFile multipartFile = new MockMultipartFile(value, value, value, value.getBytes());
        AmazonServiceException amazonServiceException = new AmazonServiceException(value);
        amazonServiceException.setStatusCode(HttpStatus.BAD_REQUEST.value());
        Mockito.when(amazonS3.putObject(Mockito.any()))
                .thenThrow(amazonServiceException);
        assertThatThrownBy(
                () -> objectStorageService.upload(multipartFile)
        ).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    public void whenUploadThrowsIOExceptionThenThrowResponseStatusException() throws IOException {
        String value = "test";
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        Mockito.when(multipartFile.getOriginalFilename()).thenReturn(value);
        Mockito.when(multipartFile.getInputStream()).thenThrow(IOException.class);
        assertThatThrownBy(
                () -> objectStorageService.upload(multipartFile)
        ).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    public void whenDownload() throws IllegalAccessException, IOException {
        String value = "test";
        Resource resource = new InputStreamResource(new ByteArrayInputStream(value.getBytes()));
        Field s3BucketNameField = ReflectionUtils
                .findField(ObjectStorageServiceImpl.class, "s3BucketName");
        s3BucketNameField.setAccessible(true);
        s3BucketNameField.set(objectStorageService, value);
        S3Object s3Object = Mockito.mock(S3Object.class);
        Mockito.when(s3Object.getObjectContent())
                .thenReturn(new S3ObjectInputStream(new ByteArrayInputStream(value.getBytes()), new HttpGet()));
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(value);
        Mockito.when(s3Object.getObjectMetadata())
                .thenReturn(objectMetadata);
        Mockito.when(amazonS3.getObject(Mockito.any()))
                .thenReturn(s3Object);
        FileDownloadResultDto expectedFileDownloadResultDto = new FileDownloadResultDto(resource, value);
        FileDownloadResultDto fileDownloadResultDto = objectStorageService.download(value);
        assertThat(fileDownloadResultDto.mediaType()).isEqualTo(expectedFileDownloadResultDto.mediaType());
        assertThat(fileDownloadResultDto.resource().getContentAsByteArray())
                .isEqualTo(expectedFileDownloadResultDto.resource().getContentAsByteArray());
        Mockito.verify(amazonS3).getObject(Mockito.any());
    }

    @Test
    public void whenDownloadThrowsAmazonS3ExceptionThenThrowResponseStatusException() {
        String value = "test";
        AmazonS3Exception amazonS3Exception = new AmazonS3Exception(value);
        amazonS3Exception.setStatusCode(HttpStatus.BAD_REQUEST.value());
        Mockito.when(amazonS3.getObject(Mockito.any()))
                .thenThrow(amazonS3Exception);
        assertThatThrownBy(
                () -> objectStorageService.download(value)
        ).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    public void whenDelete() {
        String value = "test";
        Assertions.assertAll(
                () -> objectStorageService.delete(value)
        );
    }

    @Test
    public void whenDeleteThrowsAmazonS3ExceptionThenThrowResponseStatusException() {
        String value = "test";
        Mockito.doThrow(ResponseStatusException.class)
                .when(amazonS3)
                .deleteObject(Mockito.any());
        assertThatThrownBy(
                () -> objectStorageService.delete(value)
        ).isInstanceOf(ResponseStatusException.class);
    }
}