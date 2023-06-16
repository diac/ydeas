package com.diac.ydeas.ideas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

/**
 * Конфигурация пагинации
 */
@Configuration
public class PagingConfig {

    /**
     * Бин, устанавливающий номер первой страницы пагинации в значение "1" вместо "0"
     *
     * @return Бин PageableHandlerMethodArgumentResolverCustomizer
     */
    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer paginationCustomizer() {
        return pageableResolver -> pageableResolver.setOneIndexedParameters(true);
    }
}