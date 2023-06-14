package com.diac.ydeas.domain.model;

import com.diac.ydeas.domain.enumeration.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Модель данных "Пользователь"
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class User {

    @EqualsAndHashCode.Include
    private int id;

    /**
     * Имя пользователя
     */
    @NotNull(message = "Username is required")
    @NotBlank(message = "Username cannot be blank")
    private String username;

    /**
     * Пароль
     */
    @NotNull(message = "Password is required")
    private char[] password;

    /**
     * Email пользователя
     */
    @NotNull(message = "User email is required")
    @NotBlank(message = "User email cannot be blank")
    private String email;

    /**
     * Роль пользователя
     */
    private UserRole userRole;
}