package com.diac.ydeas.ideas.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Конфигурация RestTemplate
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Создать бин RestTemplate
     *
     * @param builder Объект-строитель
     * @return Бин RestTemplate
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}