package com.habibi.modern.exceptions;

import com.habibi.modern.enums.ErrorCode;

public class SignUpWithdrawException extends SignUpException {
    public SignUpWithdrawException(ErrorCode errorCode, String message) {
        super("Error Code: " + errorCode + " And related message is: " + message);
    }
}