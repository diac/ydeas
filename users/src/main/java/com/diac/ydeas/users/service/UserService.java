package com.diac.ydeas.users.service;

import com.diac.ydeas.domain.model.User;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы с объектами User
 */
public interface UserService {

    /**
     * Найти всех пользователей
     *
     * @return Список с пользователями
     */
    List<User> findAll();

    /**
     * Найти пользователя по UUID
     *
     * @param uuid UUID пользователя
     * @return пользователь
     */
    User findByUuid(UUID uuid);
}