package com.diac.ydeas.domain.model;

import com.diac.ydeas.domain.enumeration.UserRole;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * Модель данных "Запись ACL"
 *
 * @param uri         URI запрашиваемого ресурса
 * @param httpMethods Список методов, определенных в записи
 * @param userRole    Роль пользователя, требуемая для доступа
 */
public record AclRecord(String uri, List<HttpMethod> httpMethods, UserRole userRole) {
}