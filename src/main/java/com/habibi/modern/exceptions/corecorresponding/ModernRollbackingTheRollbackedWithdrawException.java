package com.habibi.modern.exceptions.corecorresponding;


import com.habibi.modern.dto.ErrorDto;
import com.habibi.modern.enums.ErrorCode;
import com.habibi.modern.exceptions.SystemException;

public class ModernRollbackingTheRollbackedWithdrawException extends SystemException {

    public ModernRollbackingTheRollbackedWithdrawException() {
        errorCode = ErrorCode.ROLLBACKING_THE_ROLLBACKED_WITHDRAW;
    }

    public ModernRollbackingTheRollbackedWithdrawException(ErrorDto errorDto) {
        this();
        this.errorDto = errorDto;
    }
}