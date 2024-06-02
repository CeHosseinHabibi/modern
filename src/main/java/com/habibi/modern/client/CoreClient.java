package com.habibi.modern.client;

import com.habibi.modern.dto.RollbackWithdrawDto;
import com.habibi.modern.dto.WithdrawDto;
import com.habibi.modern.dto.WithdrawResponseDto;
import feign.Headers;
import feign.RequestLine;

public interface CoreClient {
    @RequestLine("POST /withdraw")
    @Headers("Content-Type: application/json")
    WithdrawResponseDto withdraw(WithdrawDto withdrawDto);

    @RequestLine("POST /rollback-withdraw")
    @Headers("Content-Type: application/json")
    void rollbackWithdraw(RollbackWithdrawDto rollbackWithdrawDto);
}
