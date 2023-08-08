package com.diac.ydeas.users.service;

import com.diac.ydeas.domain.model.User;

import java.util.List;

public interface UserService {

    List<User> findAll();
}