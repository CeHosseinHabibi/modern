package com.habibi.modern.exceptions.corecorresponding;


import com.habibi.modern.dto.ErrorDto;
import com.habibi.modern.enums.ErrorCode;
import com.habibi.modern.exceptions.SystemException;

public class ModernInsufficientFundsException extends SystemException {

    public ModernInsufficientFundsException() {
        errorCode = ErrorCode.INSUFFICIENT_FUNDS;
    }

    public ModernInsufficientFundsException(ErrorDto errorDto) {
        this();
        this.errorDto = errorDto;
    }
}