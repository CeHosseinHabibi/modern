package com.habibi.modern.exceptions;

import com.habibi.modern.dto.ErrorDto;
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
    public ResponseEntity<ErrorDto> handleTosanException(TosanException exception) {
        String message = exception.getMessage() == null ?
                messageSource.getMessage(String.valueOf(exception.getErrorCode()), null, Locale.ENGLISH) :
                exception.getMessage();
        String additionalDescription = exception.getAdditionalDescription();
        String subErrorMessage = exception.getSubErrorCode() != null ?
                messageSource.getMessage(String.valueOf(exception.getSubErrorCode()), null, Locale.ENGLISH) : null;
        message = additionalDescription == null ? message : message + " (" + additionalDescription + ")";
        message = subErrorMessage == null ? message : message + " (" + subErrorMessage + ")";
        return ResponseEntity.badRequest().body(new ErrorDto(exception.getErrorCode(), message, "Modern-Microservice",
                exception.getClass().getSimpleName(), exception.getErrorDto()));
    }
}