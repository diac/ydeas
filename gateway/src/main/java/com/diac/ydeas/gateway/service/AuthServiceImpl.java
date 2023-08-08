package com.diac.ydeas.gateway.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.diac.ydeas.domain.enumeration.UserRole;
import com.diac.ydeas.domain.model.AclRecord;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;

import java.util.*;

/**
 * Сервис аутентификации и авторизации
 */
@Service
public class AuthServiceImpl implements AuthService {

    /**
     * Подзаголовок авторизации "Bearer"
     */
    private static final String BEARER_SUBHEADER = "Bearer";

    /**
     * Колюч токена, в котором содержатся claims для realm авторизации
     */
    private static final String REALM_ACCESS_CLAIM = "realm_access";

    /**
     * Ключ токена, в котором содержатся роли пользователя, запросившего авторизацию
     */
    private static final String ROLES_KEY = "roles";

    /**
     * Получить токен из объекта ServerWebExchange
     *
     * @param exchange Объект ServerWebExchange
     * @return Токен
     * @throws IllegalArgumentException В случае, если токен не обнаружен в exchange
     */
    @Override
    public String getToken(ServerWebExchange exchange) throws IllegalArgumentException {
        return Optional.ofNullable(
                        Optional.ofNullable(exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION))
                                .orElse(Collections.emptyList())
                                .get(0)
                )
                .map(authHeader -> authHeader.replaceFirst(BEARER_SUBHEADER, "").trim())
                .orElseThrow(
                        () -> new IllegalArgumentException("Authorization header is missing")
                );
    }

    /**
     * Получить список полномочий из токена
     *
     * @param token Токен
     * @return Список полномочий
     */
    @Override
    public List<UserRole> getRolesFromToken(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        @SuppressWarnings("unchecked") Map<String, Collection<String>> realmAccess = decodedJWT.getClaim(REALM_ACCESS_CLAIM)
                .as(HashMap.class);
        Collection<String> roles = realmAccess.get(ROLES_KEY);
        return roles.stream()
                .filter(
                        role -> Arrays.stream(UserRole.values())
                                .anyMatch(roleValue -> roleValue.toString().equalsIgnoreCase(role))
                )
                .map(UserRole::valueOf)
                .toList();
    }

    /**
     * Проверить, разрешен ли обмен exchange для перечня ACL-записей
     *
     * @param exchange   Объект ServerWebExchange
     * @param aclRecords Перечень ACL-записей
     * @return true, если обмен разрешен. Иначе -- false
     */
    @Override
    public boolean exchangeIsAuthorized(ServerWebExchange exchange, List<AclRecord> aclRecords) {
        return !aclRecords.stream()
                .filter(
                        aclRecord ->
                                exchange.getRequest().getPath().toString().startsWith(aclRecord.uri())
                                        && aclRecord.httpMethods().contains(exchange.getRequest().getMethod())
                                        && getRolesFromToken(getToken(exchange)).contains(aclRecord.userRole())
                ).toList()
                .isEmpty();
    }
}