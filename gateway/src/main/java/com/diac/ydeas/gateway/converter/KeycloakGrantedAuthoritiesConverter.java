package com.diac.ydeas.gateway.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Конвертер, преобразующий роли Keycloak в объекты GrantedAuthority
 */
@RequiredArgsConstructor
public class KeycloakGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    /**
     * Ключ "roles" в JWT-токене Keycloak
     */
    private static final String ROLES = "roles";

    /**
     * Ключ "realm_access" в JWT-токене Keycloak
     */
    private static final String CLAIM_REALM_ACCESS = "realm_access";

    /**
     * Конвертер, используемый по умолчанию
     */
    private final Converter<Jwt, Collection<GrantedAuthority>> defaultAuthoritiesConverter
            = new JwtGrantedAuthoritiesConverter();

    /**
     * Метод, конвертирующий роли в JWT-токене Keycloak в коллекцию объектов GrantedAuthority
     *
     * @param jwt JWT-токен Keycloak
     * @return Коллекция объектов GrantedAuthority
     */
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        var realmRoles = realmRoles(jwt);
        Collection<GrantedAuthority> authorities = realmRoles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
        authorities.addAll(defaultGrantedAuthorities(jwt));
        return authorities;
    }

    /**
     * Метод, возвращающий коллекцию объектов GrantedAuthority, полученную из JWT-токена с использованием
     * конвертера по умолчанию
     *
     * @param jwt JWT-токен
     * @return Коллекция объектов GrantedAuthority
     */
    private Collection<GrantedAuthority> defaultGrantedAuthorities(Jwt jwt) {
        return Optional.ofNullable(defaultAuthoritiesConverter.convert(jwt))
                .orElse(Collections.emptyList());
    }

    /**
     * Получить роли в формате строк из JWT-токена
     *
     * @param jwt JWT-токен
     * @return Список, содержащий роли в формате строк
     */
    private List<String> realmRoles(Jwt jwt) {
        return Optional.ofNullable(jwt.getClaimAsMap(CLAIM_REALM_ACCESS))
                .map(realmAccess -> (List<String>) realmAccess.get(ROLES))
                .orElse(Collections.emptyList());
    }
}