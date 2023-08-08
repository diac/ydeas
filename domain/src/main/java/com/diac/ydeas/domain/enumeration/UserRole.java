package com.diac.ydeas.domain.enumeration;

/**
 * Модель данных "Роль пользователя"
 */
public enum UserRole {

    /**
     * Пользователь
     */
    USER,

    /**
     * Эксперт
     */
    EXPERT;

    public static boolean contains(String test) {

        for (UserRole userRole : UserRole.values()) {
            if (userRole.name().equals(test)) {
                return true;
            }
        }

        return false;
    }
}