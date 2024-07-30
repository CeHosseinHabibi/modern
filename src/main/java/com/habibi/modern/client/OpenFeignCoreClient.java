package com.habibi.modern.client;

import com.habibi.modern.configuration.OpenFeignConfig;
import com.habibi.modern.dto.RollBackWithdrawResponseDto;
import com.habibi.modern.dto.RollbackWithdrawDto;
import com.habibi.modern.dto.WithdrawDto;
import com.habibi.modern.dto.WithdrawResponseDto;
import com.habibi.modern.exceptions.CoreInvocationException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "CoreClient", url = "${microservice.core.url}", configuration = OpenFeignConfig.class)
public interface OpenFeignCoreClient {
    @PostMapping("${microservice.core.withdraw.url}")
    WithdrawResponseDto callWithdraw(WithdrawDto withdrawDto) throws CoreInvocationException;

    @PostMapping("${microservice.core.rollback.withdraw.url}")
    RollBackWithdrawResponseDto callRollBack(RollbackWithdrawDto rollbackWithdrawDto) throws CoreInvocationException;
}