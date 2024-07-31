package com.habibi.modern.exceptions.corecorresponding;


import com.habibi.modern.dto.ErrorDto;
import com.habibi.modern.enums.ErrorCode;
import com.habibi.modern.exceptions.SystemException;

public class ModernWithdrawOfRollbackNotFoundException extends SystemException {

    public ModernWithdrawOfRollbackNotFoundException() {
        errorCode = ErrorCode.WITHDRAW_OF_ROLLBACK_NOT_FOUND;
    }

    public ModernWithdrawOfRollbackNotFoundException(ErrorDto errorDto) {
        this();
        this.errorDto = errorDto;
    }
}