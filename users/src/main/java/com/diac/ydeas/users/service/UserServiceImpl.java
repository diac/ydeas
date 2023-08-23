package com.diac.ydeas.users.service;

import com.diac.ydeas.domain.enumeration.UserRole;
import com.diac.ydeas.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сервис для работы с объектами User
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    /**
     * Бин Keycloak
     */
    private final Keycloak keycloak;

    /**
     * Имя рэлма в Keycloak
     */
    @Value("${keycloak.realmName}")
    private String realmName;

    /**
     * Найти всех пользователей
     *
     * @return Список с пользователями
     */
    @Override
    public List<User> findAll() {
        final RealmResource realmResource = keycloak.realm(realmName);
        return realmResource
                .users()
                .list()
                .stream()
                .map(
                        userRepresentation -> {
                            String userId = userRepresentation.getId();
                            Set<UserRole> userRoles = realmResource
                                    .users()
                                    .get(userId)
                                    .roles()
                                    .realmLevel()
                                    .listEffective()
                                    .stream()
                                    .filter(
                                            roleRepresentation -> UserRole.contains(roleRepresentation.getName())
                                    )
                                    .map(
                                            roleRepresentation -> UserRole.valueOf(roleRepresentation.getName())
                                    )
                                    .collect(Collectors.toSet());
                            return User.builder()
                                    .uuid(UUID.fromString(userRepresentation.getId()))
                                    .username(userRepresentation.getUsername())
                                    .email(userRepresentation.getEmail())
                                    .firstName(userRepresentation.getFirstName())
                                    .lastName(userRepresentation.getLastName())
                                    .userRoles(userRoles)
                                    .build();
                        }
                ).collect(Collectors.toList());
    }
}