package com.diac.ydeas.domain.model;

import com.diac.ydeas.domain.enumeration.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;
import java.util.UUID;

/**
 * Модель данных "Пользователь"
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class User {

    /**
     * Идентификатор пользователя
     */
    @EqualsAndHashCode.Include
    private UUID uuid;

    /**
     * Имя пользователя
     */
    @NotNull(message = "Username is required")
    @NotBlank(message = "Username cannot be blank")
    private String username;

    /**
     * Email пользователя
     */
    @NotNull(message = "User email is required")
    @NotBlank(message = "User email cannot be blank")
    private String email;

    private String firstName;

    private String lastName;

    /**
     * Роли пользователя
     */
    private Set<UserRole> userRoles;
}