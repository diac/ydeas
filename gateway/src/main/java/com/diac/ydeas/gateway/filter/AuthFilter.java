package com.diac.ydeas.gateway.filter;

import com.diac.ydeas.domain.enumeration.Authority;
import com.diac.ydeas.domain.model.AclRecord;
import com.diac.ydeas.gateway.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Фильтр авторизации
 */
@Component
@Slf4j
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    /**
     * Перечень HTTP-методов чтения
     */
    private static final List<HttpMethod> HTTP_READ_METHODS = List.of(HttpMethod.GET);

    /**
     * Перечень HTTP-методов записи
     */
    private static final List<HttpMethod> HTTP_WRITE_METHODS = List.of(
            HttpMethod.GET,
            HttpMethod.POST,
            HttpMethod.PUT,
            HttpMethod.PATCH,
            HttpMethod.DELETE
    );

    /**
     * Перечень ACL-записей шлюза
     */
    private static final List<AclRecord> ACL = List.of(
            new AclRecord("/ideas", HTTP_READ_METHODS, Authority.IDEAS_USER),
            new AclRecord("/ideas", HTTP_READ_METHODS, Authority.IDEAS_EXPERT)
    );

    /**
     * Сервис аутентификации и авторизации
     */
    private final AuthService authService;

    /**
     * Конструктор класса
     *
     * @param authService Сервис аутентификации и авторизации
     */
    public AuthFilter(AuthService authService) {
        super(Config.class);
        this.authService = authService;
    }

    /**
     * Метод, реализующий логику фильтра
     *
     * @param config Объект Config
     * @return Объект GatewayFilter с встроенной в цепочку логикой авторизации
     */
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (!authService.exchangeIsAuthorized(exchange, ACL)) {
                log.warn("Unauthorized");
                throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Unauthorized"
                );
            }
            return chain.filter(exchange);
        });
    }

    /**
     * Внутренний класс конфигурации
     */
    public static class Config {

    }
}