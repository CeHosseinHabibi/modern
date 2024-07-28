package com.habibi.modern.exceptions;

import com.habibi.modern.enums.ErrorCode;

public class RollbackWithdrawException extends SignUpException {
    public RollbackWithdrawException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}