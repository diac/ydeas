package com.diac.ydeas.gateway.service;

import com.diac.ydeas.domain.enumeration.Authority;
import com.diac.ydeas.domain.model.AclRecord;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

/**
 * Сервис аутентификации и авторизации
 */
public interface AuthService {

    /**
     * Получить токен из объекта ServerWebExchange
     *
     * @param exchange Объект ServerWebExchange
     * @return Токен
     * @throws IllegalArgumentException В случае, если токен не обнаружен в exchange
     */
    String getToken(ServerWebExchange exchange) throws IllegalArgumentException;

    /**
     * Получить список полномочий из токена
     *
     * @param token Токен
     * @return Список полномочий
     */
    List<Authority> getAuthoritiesFromToken(String token);

    /**
     * Проверить, разрешен ли обмен exchange для перечня ACL-записей
     *
     * @param exchange   Объект ServerWebExchange
     * @param aclRecords Перечень ACL-записей
     * @return true, если обмен разрешен. Иначе -- false
     */
    boolean exchangeIsAuthorized(ServerWebExchange exchange, List<AclRecord> aclRecords);
}