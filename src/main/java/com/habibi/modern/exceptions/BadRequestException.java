package com.habibi.modern.exceptions;

import com.habibi.modern.enums.ErrorCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BadRequestException extends SystemException {
    public BadRequestException(String... additionalDescription) {
        this.errorCode = ErrorCode.BAD_REQUEST_EXCEPTION;
        this.additionalDescription = additionalDescription;
    }
}