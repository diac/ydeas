package com.diac.ydeas.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Исключение, выбрасываемое в случае, при обращении к ресурсу нарушаются наложенные на него ограничения
 */
@RequiredArgsConstructor
@Getter
public class ResourceConstraintViolationException extends RuntimeException {

    /**
     * Текст пояснительного сообщения в исключении
     */
    private final String message;
}