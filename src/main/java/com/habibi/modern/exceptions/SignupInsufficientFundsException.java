package com.habibi.modern.exceptions;


import com.habibi.modern.dto.ErrorDto;
import com.habibi.modern.enums.ErrorCode;

public class SignupInsufficientFundsException extends SystemException {

    public SignupInsufficientFundsException() {
        errorCode = ErrorCode.SIGNUP_INSUFFICIENT_FUNDS;
    }

    public SignupInsufficientFundsException(ErrorDto errorDto) {
        this();
        this.errorDto = errorDto;
    }
}