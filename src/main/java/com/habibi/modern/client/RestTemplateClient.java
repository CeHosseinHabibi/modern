package com.habibi.modern.client;

import com.habibi.modern.dto.*;
import com.habibi.modern.enums.ErrorCode;
import com.habibi.modern.exceptions.RollbackWithdrawException;
import com.habibi.modern.exceptions.SignUpWithdrawException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static com.habibi.modern.util.Utils.*;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "rest.service", havingValue = "rest-template")
public class RestTemplateClient implements ModernRestClient {

    private final RestTemplate restTemplate;
    @Value("${core.registration.fee}")
    private Long registrationFee;

    public WithdrawResponseDto callWithdraw(Long accountNumber, RequesterDto requesterDto)
            throws SignUpWithdrawException {
        try {
            return restTemplate.postForEntity(CORE_SERVICE_URL + SLASH + ACCOUNTS + SLASH + WITHDRAW,
                    new WithdrawDto(accountNumber, registrationFee, requesterDto), WithdrawResponseDto.class).getBody();
        } catch (ResourceAccessException resourceAccessException) {
            throw new SignUpWithdrawException(ErrorCode.CORE_IS_UNREACHABLE, "A connection problem with core system");
        } catch (HttpClientErrorException httpClientErrorException) {
            WithdrawResponseDto exceptionBody = httpClientErrorException.getResponseBodyAs(WithdrawResponseDto.class);
            throw new SignUpWithdrawException(exceptionBody.getErrorCode(), exceptionBody.getDescription());
        } catch (HttpServerErrorException httpServerErrorException) {
            throw new SignUpWithdrawException(ErrorCode.CORE_THROWS_INTERNAL_SERVER_ERROR,
                    "Core system -> withdraw returned 500 statues code.");
        }
    }

    public RollBackWithdrawResponseDto callRollBack(RollbackWithdrawDto rollbackWithdrawDto)
            throws RollbackWithdrawException {
        try {
            return restTemplate.postForEntity(CORE_SERVICE_URL + SLASH + ACCOUNTS + SLASH + ROLLBACK_WITHDRAW,
                    rollbackWithdrawDto, RollBackWithdrawResponseDto.class).getBody();
        } catch (ResourceAccessException resourceAccessException) {
            throw new RollbackWithdrawException(ErrorCode.CORE_IS_UNREACHABLE, "A connection problem with core system");
        } catch (HttpClientErrorException httpClientErrorException) {
            RollBackWithdrawResponseDto exceptionBody =
                    httpClientErrorException.getResponseBodyAs(RollBackWithdrawResponseDto.class);
            throw new RollbackWithdrawException(exceptionBody.getErrorCode(), exceptionBody.getDescription());
        } catch (HttpServerErrorException httpServerErrorException) {
            throw new RollbackWithdrawException(ErrorCode.CORE_THROWS_INTERNAL_SERVER_ERROR,
                    "Core system -> rollback-withdraw returned 500 statues code.");
        }
    }
}
