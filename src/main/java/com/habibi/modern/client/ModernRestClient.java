package com.habibi.modern.client;

import com.habibi.modern.dto.RequesterDto;
import com.habibi.modern.dto.RollBackWithdrawResponseDto;
import com.habibi.modern.dto.RollbackWithdrawDto;
import com.habibi.modern.dto.WithdrawResponseDto;
import com.habibi.modern.exceptions.CoreInvocationException;

public interface ModernRestClient {
    WithdrawResponseDto callWithdraw(Long accountNumber, RequesterDto requesterDto) throws CoreInvocationException;

    RollBackWithdrawResponseDto callRollBack(RollbackWithdrawDto rollbackWithdrawDto) throws CoreInvocationException;
}