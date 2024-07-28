package com.habibi.modern.client;

import com.habibi.modern.dto.*;
import com.habibi.modern.enums.ErrorCode;
import com.habibi.modern.exceptions.RollbackWithdrawException;
import com.habibi.modern.exceptions.SignUpWithdrawException;
import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "rest.service", havingValue = "open-feign")
public class OpenFeignClient implements ModernRestClient {
    private final OpenFeignCoreClient openFeignCoreClient;
    @Value("${core.registration.fee}")
    private Long registrationFee;

    public WithdrawResponseDto callWithdraw(Long accountNumber, RequesterDto requesterDto) throws SignUpWithdrawException {
        try {
            return openFeignCoreClient.callWithdraw(new WithdrawDto(accountNumber, registrationFee, requesterDto));
        } catch (RetryableException exception) {
            throw new SignUpWithdrawException(ErrorCode.CORE_IS_UNREACHABLE, "A connection problem with core system");
        } catch (SignUpWithdrawException signUpWithdrawException) {
            throw signUpWithdrawException;
        }
    }

    public RollBackWithdrawResponseDto callRollBack(RollbackWithdrawDto rollbackWithdrawDto) throws RollbackWithdrawException {
        try {
            return openFeignCoreClient.callRollBack(rollbackWithdrawDto);
        } catch (RetryableException exception) {
            throw new RollbackWithdrawException(ErrorCode.CORE_IS_UNREACHABLE, "A connection problem with core system");
        } catch (RollbackWithdrawException rollbackWithdrawException) {
            throw rollbackWithdrawException;
        }
    }
}
