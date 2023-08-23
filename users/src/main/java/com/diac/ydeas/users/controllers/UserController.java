package com.diac.ydeas.users.controllers;

import com.diac.ydeas.domain.model.User;
import com.diac.ydeas.users.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер, реализующий доступ к объектам модели User
 */
@RestController
@RequestMapping("")
@AllArgsConstructor
public class UserController {

    /**
     * Сервис для работы с объектами User
     */
    private final UserService userService;

    /**
     * Получить список всех пользователей
     *
     * @return Тело ответа со списком пользователей
     */
    @RequestMapping("")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok()
                .body(userService.findAll());
    }
}