package com.habibi.modern.client;

import com.habibi.modern.dto.*;
import com.habibi.modern.enums.ErrorCode;
import com.habibi.modern.exceptions.CoreInvocationException;
import com.habibi.modern.exceptions.corecorresponding.ModernInsufficientFundsException;
import com.habibi.modern.exceptions.corecorresponding.ModernRollbackingTheRollbackedWithdrawException;
import com.habibi.modern.exceptions.corecorresponding.ModernWithdrawOfRollbackNotFoundException;
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

    public WithdrawResponseDto callWithdraw(Long accountNumber, RequesterDto requesterDto)
            throws ModernInsufficientFundsException, CoreInvocationException {
        try {
            return openFeignCoreClient.callWithdraw(new WithdrawDto(accountNumber, registrationFee, requesterDto));
        } catch (RetryableException exception) {
            throw new CoreInvocationException(ErrorCode.CORE_IS_UNREACHABLE);
        } catch (ModernInsufficientFundsException modernInsufficientFundsException) {
            throw modernInsufficientFundsException;
        }
    }

    public RollBackWithdrawResponseDto callRollBack(RollbackWithdrawDto rollbackWithdrawDto)
            throws CoreInvocationException, ModernWithdrawOfRollbackNotFoundException,
            ModernRollbackingTheRollbackedWithdrawException {
        try {
            return openFeignCoreClient.callRollBack(rollbackWithdrawDto);
        } catch (RetryableException exception) {
            throw new CoreInvocationException(ErrorCode.CORE_IS_UNREACHABLE);
        } catch (CoreInvocationException coreInvocationException) {
            throw coreInvocationException;
        } catch (ModernWithdrawOfRollbackNotFoundException modernWithdrawOfRollbackNotFoundException) {
            throw modernWithdrawOfRollbackNotFoundException;
        } catch (ModernRollbackingTheRollbackedWithdrawException modernRollbackingTheRollbackedWithdrawException) {
            throw modernRollbackingTheRollbackedWithdrawException;
        }
    }
}
