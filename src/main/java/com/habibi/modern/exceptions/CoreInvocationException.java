package com.habibi.modern.exceptions;

import com.habibi.modern.enums.ErrorCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CoreInvocationException extends InvocationException {
    public CoreInvocationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}