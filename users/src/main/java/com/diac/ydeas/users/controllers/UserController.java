package com.diac.ydeas.users.controllers;

import com.diac.ydeas.domain.model.User;
import com.diac.ydeas.users.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @RequestMapping("")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok()
                .body(userService.findAll());
    }
}