package com.diac.ydeas.users.controllers;

import com.diac.ydeas.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер, реализующий доступ к объектам модели User
 */
@Tag(name = "UserController", description = "Контроллер, реализующий доступ к объектам модели User")
public interface UserController {

    /**
     * Получить список всех пользователей
     *
     * @return Список пользователей
     */
    @GetMapping("")
    @Operation(summary = "Получить список всех пользователей")
    List<User> findAll();

    /**
     * Найти пользователя по UUID
     *
     * @param uuid UUID пользователя
     * @return Пользователь
     */
    @GetMapping("/{uuid}")
    @Operation(summary = "Найти пользователя по UUID")
    User findByUuid(
            @Parameter(description = "UUID пользователя") UUID uuid
    );
}