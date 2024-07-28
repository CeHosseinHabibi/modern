package com.habibi.modern.exceptions;

import com.habibi.modern.enums.ErrorCode;
import lombok.Getter;

@Getter
public class SignUpException extends Exception {
    ErrorCode errorCode;

    public SignUpException(String message) {
        super(message);
    }

    public SignUpException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}