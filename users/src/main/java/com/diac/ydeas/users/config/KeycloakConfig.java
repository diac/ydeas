package com.diac.ydeas.users.config;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация Keycloak
 */
@Configuration
public class KeycloakConfig {

    /**
     * URL сервера Keycloak
     */
    @Value("${keycloak.serverUrl}")
    private String serverUrl;

    /**
     * Имя рэлма в Keycloak
     */
    @Value("${keycloak.realmName}")
    private String realmName;

    /**
     * Идентификатор сервисного аккаунта Keycloak
     */
    @Value("${keycloak.clientId}")
    private String clientId;

    /**
     * Секрет сервисного аккаунта Keycloak
     */
    @Value("${keycloak.clientSecret}")
    private String clientSecret;

    /**
     * Сформировать бин Keycloak
     *
     * @return Бин Keycloak
     */
    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realmName)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .resteasyClient(
                        new ResteasyClientBuilderImpl()
                                .connectionPoolSize(10)
                                .build()
                ).build();
    }
}