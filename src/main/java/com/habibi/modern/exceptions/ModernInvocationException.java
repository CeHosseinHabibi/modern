package com.habibi.modern.exceptions;

import com.habibi.modern.enums.ErrorCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ModernInvocationException extends InvocationException {
    public ModernInvocationException(ErrorCode errorCode, String message, String... additionalDescription) {
        this.errorCode = errorCode;
        this.message = message;
        this.additionalDescription = additionalDescription;
    }
}