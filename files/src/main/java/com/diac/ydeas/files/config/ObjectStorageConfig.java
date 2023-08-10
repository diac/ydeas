package com.diac.ydeas.files.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация хранилища объектов
 */
@Configuration
public class ObjectStorageConfig {

    /**
     * Идентификатор ключа доступа
     */
    @Value("${s3.access.key.id}")
    private String accessKeyId;

    /**
     * Секрет ключа доступа
     */
    @Value("${s3.access.key.secret}")
    private String accessKeySecret;

    /**
     * Адрес эндпоинта S3
     */
    @Value("${s3.host.name}")
    private String s3Hostname;

    /**
     * Имя региона S3
     */
    @Value("${s3.region.name}")
    private String s3RegionName;

    /**
     * Создать бин AmazonS3 с настройками доступа
     *
     * @return Бин AmazonS3
     */
    @Bean
    public AmazonS3 getAmazonS3Client() {
        final AWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKeyId, accessKeySecret);
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(
                                s3Hostname,
                                s3RegionName
                        )
                )
                .build();
    }
}