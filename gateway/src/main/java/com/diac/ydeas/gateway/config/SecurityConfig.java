package com.diac.ydeas.gateway.config;

import com.diac.ydeas.domain.enumeration.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Конфигурация безопасности
 */
@Component
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Шаблон пути к API оценки идей экспертами
     */
    private static final String IDEA_REVIEW_PATH_PATTERN = "/ideas/idea_review/**";

    /**
     * Перечень разрешенных доменов CORS
     */
    @Value("${cors.allowed-origins}")
    private List<String> corsAllowedOrigins;

    /**
     * Перечень разрешенных методов CORS
     */
    @Value("${cors.allowed-methods}")
    private List<String> corsAllowedMethods;

    /**
     * Перечень разрешенных заголовков CORS
     */
    @Value("${cors.allowed-headers}")
    private List<String> corsAllowedHeaders;

    /**
     * Цепочка фильтров безопасности
     *
     * @param http                       Объект ServerHttpSecurity
     * @param jwtAuthenticationConverter Конвертер, преобразующий данные из JWT-токена в нужный формат
     * @return Цепочка SecurityWebFilterChain
     */
    @Bean
    public SecurityWebFilterChain filterChain(
            ServerHttpSecurity http,
            Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter
    ) {
        // @formatter:off
        http
                .authorizeExchange()
                .pathMatchers(IDEA_REVIEW_PATH_PATTERN).hasAuthority(
                        UserRole.EXPERT.name()
                )
                .anyExchange().hasAnyAuthority(
                        UserRole.USER.name(),
                        UserRole.EXPERT.name()
                );
        http
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(jwtAuthenticationConverter);
        http
                .cors().configurationSource(request -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOrigins(corsAllowedOrigins);
                    configuration.setAllowedMethods(corsAllowedMethods);
                    configuration.setAllowedHeaders(corsAllowedHeaders);
                    return configuration;
                });
        http.csrf().disable();
        // @formatter:on
        return http.build();
    }
}