package com.habibi.modern.client;

import com.habibi.modern.dto.RequesterDto;
import com.habibi.modern.dto.RollBackWithdrawResponseDto;
import com.habibi.modern.dto.RollbackWithdrawDto;
import com.habibi.modern.dto.WithdrawResponseDto;
import com.habibi.modern.exceptions.CoreInvocationException;
import com.habibi.modern.exceptions.corecorresponding.ModernInsufficientFundsException;
import com.habibi.modern.exceptions.corecorresponding.ModernRollbackingTheRollbackedWithdrawException;
import com.habibi.modern.exceptions.corecorresponding.ModernWithdrawOfRollbackNotFoundException;

public interface ModernRestClient {
    WithdrawResponseDto callWithdraw(Long accountNumber, RequesterDto requesterDto) throws CoreInvocationException,
            ModernInsufficientFundsException;

    RollBackWithdrawResponseDto callRollBack(RollbackWithdrawDto rollbackWithdrawDto) throws CoreInvocationException,
            ModernWithdrawOfRollbackNotFoundException, ModernRollbackingTheRollbackedWithdrawException;
}