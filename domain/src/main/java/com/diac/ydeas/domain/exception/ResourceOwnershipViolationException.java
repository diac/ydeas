package com.diac.ydeas.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Исключение, выбрасываемое в случае нарушения прав доступа к ресурсу
 */
@RequiredArgsConstructor
@Getter
public class ResourceOwnershipViolationException extends RuntimeException {

    /**
     * Текст пояснительного сообщения в исключении
     */
    private final String message;
}