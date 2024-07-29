package com.habibi.modern.exceptions;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {

    private MessageSource messageSource;

    @ExceptionHandler(value = TosanException.class)
    public ResponseEntity<ModernInvocationException> handleTosanException(TosanException exception) {
        String message = exception.getMessage() == null ?
                messageSource.getMessage(String.valueOf(exception.getErrorCode()), null, Locale.ENGLISH) :
                exception.getMessage();
        ModernInvocationException modernInvocationException =
                new ModernInvocationException(exception.getErrorCode(), message, exception.getAdditionalDescription());
        modernInvocationException.setStackTrace(new StackTraceElement[0]);
        return ResponseEntity.badRequest().body(modernInvocationException);
    }
}