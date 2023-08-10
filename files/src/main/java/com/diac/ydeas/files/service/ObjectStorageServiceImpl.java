package com.diac.ydeas.files.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.diac.ydeas.domain.dto.FileDownloadResultDto;
import com.diac.ydeas.domain.dto.StoredObjectDetailsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Date;
import java.util.function.Function;

/**
 * Сервис для работы с хранилищем объектов
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ObjectStorageServiceImpl implements ObjectStorageService {

    /**
     * Объект-хранилище AmazonS3
     */
    private final AmazonS3 amazonS3;

    /**
     * Имя бакета в хранилище
     */
    @Value("${s3.bucket.name}")
    private String s3BucketName;

    /**
     * Функция формирования имени загружаемого файла
     */
    private final Function<String, String> targetFileNameFunction = (fileName) ->
            targetFileName(targetFileName(fileName));

    /**
     * Загрузить объект в хранилище
     *
     * @param multipartFile загружаемый объект
     * @return DTO с деталями хранимого объекта
     */
    @Override
    @Async
    public StoredObjectDetailsDto upload(MultipartFile multipartFile) {
        try {
            final ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(multipartFile.getSize());
            objectMetadata.setContentType(multipartFile.getContentType());
            final String targetFileName = targetFileNameFunction.apply(multipartFile.getOriginalFilename());
            final PutObjectRequest putObjectRequest = new PutObjectRequest(
                    s3BucketName,
                    targetFileName,
                    multipartFile.getInputStream(),
                    objectMetadata
            );
            amazonS3.putObject(putObjectRequest);
            return new StoredObjectDetailsDto(
                    s3BucketName,
                    targetFileName,
                    multipartFile.getContentType(),
                    multipartFile.getSize()
            );
        } catch (AmazonServiceException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(e.getStatusCode(), e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * Загрузить файл из хранилища
     *
     * @param fileName Имя файла
     * @return Объект FileDownloadResultDto с результатом загрузки файла
     * @throws ResponseStatusException в случае, если не удалось загрузить файл из хранилища
     */
    @Override
    @Async
    public FileDownloadResultDto download(String fileName) {
        try {
            GetObjectRequest getObjectRequest = new GetObjectRequest(s3BucketName, fileName);
            S3Object s3Object = amazonS3.getObject(getObjectRequest);
            return new FileDownloadResultDto(
                    new InputStreamResource(s3Object.getObjectContent()),
                    s3Object.getObjectMetadata().getContentType()
            );
        } catch (AmazonS3Exception e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(e.getStatusCode(), e.getMessage(), e);
        }
    }

    /**
     * Удалить файл из хранилища
     *
     * @param fileName Имя файла
     * @throws ResponseStatusException в случае, если не удалось удалить файл из хранилища
     */
    @Override
    @Async
    public void delete(String fileName) {
        try {
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(s3BucketName, fileName);
            amazonS3.deleteObject(deleteObjectRequest);
        } catch (AmazonS3Exception e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(e.getStatusCode(), e.getMessage(), e);
        }
    }

    /**
     * Сформировать имя целевого файла в хранилище на основе оригинального имени файла
     *
     * @param originalFilename Оригинальное имя файла
     * @return Новое имя файла
     */
    private String targetFileName(String originalFilename) {
        StringBuffer stringBuffer = new StringBuffer();
        int extensionPosition = originalFilename.lastIndexOf(".");
        int splitIndex = extensionPosition > 0 ? extensionPosition : originalFilename.length();
        stringBuffer.append(
                originalFilename,
                0,
                splitIndex
        );
        stringBuffer.append(targetFileNameSuffix());
        stringBuffer.append(originalFilename
                .substring(splitIndex)
        );
        return stringBuffer.toString();
    }

    /**
     * Сформировать суффикс для загружаемого файла
     *
     * @return Суффикс для загружаемого файла
     */
    private String targetFileNameSuffix() {
        return String.valueOf(new Date().getTime());
    }
}