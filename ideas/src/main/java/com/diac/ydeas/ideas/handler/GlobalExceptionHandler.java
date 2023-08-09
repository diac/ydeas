package com.diac.ydeas.ideas.handler;

import com.diac.ydeas.domain.exception.ResourceConstraintViolationException;
import com.diac.ydeas.domain.exception.ResourceNotFoundException;
import com.diac.ydeas.domain.exception.ResourceOwnershipViolationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.HashMap;

/**
 * Глобальный обработчик исключений
 */
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Маппер объектов
     */
    private final ObjectMapper objectMapper;

    /**
     * Метод-обработчик исключений ResourceNotFoundException
     *
     * @param e Объект-исключение
     * @throws IOException В случае, если возникает ошибка записи в response
     */
    @ExceptionHandler(
            value = {
                    ResourceNotFoundException.class
            }
    )
    public void handleResourceNotFoundException(Exception e, HttpServletResponse response) throws IOException {
        log.warn(e.getMessage(), e);
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(
                new HashMap<>() {
                    {
                        put("message", e.getMessage());
                        put("type", e.getClass());
                    }
                }
        ));
    }

    /**
     * Метод-обработчик исключений ResourceConstraintViolationException
     *
     * @param e Объект-исключение
     * @throws IOException В случае, если возникает ошибка записи в response
     */
    @ExceptionHandler(
            value = {
                    ResourceConstraintViolationException.class
            }
    )
    public void handleResourceConstraintViolationException(
            Exception e,
            HttpServletResponse response
    ) throws IOException {
        log.warn(e.getMessage(), e);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(
                new HashMap<>() {
                    {
                        put("message", e.getMessage());
                        put("type", e.getClass());
                    }
                }
        ));
    }

    /**
     * Метод-обработчик исключений ResourceOwnershipViolationException
     *
     * @param e Объект-исключение
     * @throws IOException В случае, если возникает ошибка записи в response
     */
    @ExceptionHandler(
            value = {
                    ResourceOwnershipViolationException.class
            }
    )
    public void handleResourceOwnershipViolationException(
            Exception e,
            HttpServletResponse response
    ) throws IOException {
        log.warn(e.getMessage(), e);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(
                new HashMap<>() {
                    {
                        put("message", e.getMessage());
                        put("type", e.getClass());
                    }
                }
        ));
    }
}