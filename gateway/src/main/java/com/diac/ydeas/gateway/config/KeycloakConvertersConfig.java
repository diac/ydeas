package com.diac.ydeas.gateway.config;

import com.diac.ydeas.gateway.converter.KeycloakGrantedAuthoritiesConverter;
import com.diac.ydeas.gateway.converter.ReactiveKeycloakJwtAuthenticationConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

import java.util.Collection;

/**
 * Конфигурация конвертеров для Keycloak
 */
@Configuration
public class KeycloakConvertersConfig {

    /**
     * Конвертер, преобразующий роли Keycloak в объекты GrantedAuthority
     */
    @Bean
    public Converter<Jwt, Collection<GrantedAuthority>> keycloakGrantedAuthoritiesConverter() {
        return new KeycloakGrantedAuthoritiesConverter();
    }

    /**
     * Конвертер, преобразующий данные аутентификации из формата KeyCloak в AbstractAuthenticationToken
     */
    @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> keycloakJwtAuthenticationConverter(
            Converter<Jwt, Collection<GrantedAuthority>> converter
    ) {
        return new ReactiveKeycloakJwtAuthenticationConverter(converter);
    }
}