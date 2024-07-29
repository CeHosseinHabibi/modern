package com.habibi.modern.client;

import com.habibi.modern.configuration.OpenFeignConfig;
import com.habibi.modern.dto.RollBackWithdrawResponseDto;
import com.habibi.modern.dto.RollbackWithdrawDto;
import com.habibi.modern.dto.WithdrawDto;
import com.habibi.modern.dto.WithdrawResponseDto;
import com.habibi.modern.exceptions.CoreInvocationException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import static com.habibi.modern.util.Utils.*;

@FeignClient(name = "CoreClient", url = CORE_SERVICE_URL, configuration = OpenFeignConfig.class)
public interface OpenFeignCoreClient {
    @PostMapping(SLASH + ACCOUNTS + SLASH + WITHDRAW)
    WithdrawResponseDto callWithdraw(WithdrawDto withdrawDto) throws CoreInvocationException;

    @PostMapping(SLASH + ACCOUNTS + SLASH + ROLLBACK_WITHDRAW)
    RollBackWithdrawResponseDto callRollBack(RollbackWithdrawDto rollbackWithdrawDto) throws CoreInvocationException;
}
