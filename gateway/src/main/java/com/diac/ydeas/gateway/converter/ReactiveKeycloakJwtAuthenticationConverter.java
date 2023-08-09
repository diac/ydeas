package com.diac.ydeas.gateway.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtGrantedAuthoritiesConverterAdapter;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

/**
 * Конвертер, преобразующий данные аутентификации из формата KeyCloak в AbstractAuthenticationToken
 */
public final class ReactiveKeycloakJwtAuthenticationConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    /**
     * Ключ "preferred_username" JWT-токене Keycloak
     */
    private static final String USERNAME_CLAIM = "preferred_username";

    /**
     * Конвертер, преобразующий роли Keycloak в объекты GrantedAuthority
     */
    private final Converter<Jwt, Flux<GrantedAuthority>> jwtGrantedAuthoritiesConverter;

    /**
     * Конструктор класса
     *
     * @param jwtGrantedAuthoritiesConverter Конвертер, преобразующий роли Keycloak в объекты GrantedAuthority
     */
    public ReactiveKeycloakJwtAuthenticationConverter(Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter) {
        Assert.notNull(jwtGrantedAuthoritiesConverter, "jwtGrantedAuthoritiesConverter cannot be null");
        this.jwtGrantedAuthoritiesConverter = new ReactiveJwtGrantedAuthoritiesConverterAdapter(
                jwtGrantedAuthoritiesConverter);
    }

    /**
     * Метод, извлекающий данные аутентификации из JWT-токена и оборачивающий их в Mono<AbstractAuthenticationToken>
     *
     * @param jwt JWT-токен
     * @return Объект Mono<AbstractAuthenticationToken>, содержащий данные аутентификации
     */
    @Override
    public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
        // @formatter:off
        return this.jwtGrantedAuthoritiesConverter.convert(jwt)
                .collectList()
                .map((authorities) -> new JwtAuthenticationToken(jwt, authorities, extractUsername(jwt)));
        // @formatter:on
    }

    /**
     * Метод, извлекающий имя пользователя из JWT-токена
     *
     * @param jwt JWT-токен
     * @return Имя пользователя
     */
    protected String extractUsername(Jwt jwt) {
        return jwt.hasClaim(USERNAME_CLAIM) ? jwt.getClaimAsString(USERNAME_CLAIM) : jwt.getSubject();
    }

}