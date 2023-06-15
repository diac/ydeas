package com.diac.ydeas.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Исключение, выбрасываемое в случае, если запрашиваемый ресурс не найден
 */
@RequiredArgsConstructor
@Getter
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Текст пояснительного сообщения в исключении
     */
    private final String message;
}