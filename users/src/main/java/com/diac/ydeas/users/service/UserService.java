package com.diac.ydeas.users.service;

import com.diac.ydeas.domain.model.User;

import java.util.List;

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


}