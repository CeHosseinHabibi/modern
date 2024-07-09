package com.habibi.modern.client;

import com.habibi.modern.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class RestTemplateClient {
    private static final String CORE_SERVICE_URL = "http://localhost:8081";
    private static final String ACCOUNTS = "accounts";
    private static final String SLASH = "/";
    private static final String WITHDRAW = "withdraw";
    private static final String ROLLBACK_WITHDRAW = "rollback-withdraw";
    private final RestTemplate restTemplate;
    @Value("${core.registration.fee}")
    private Long registrationFee;

    public WithdrawResponseDto callWithdraw(Long accountNumber, RequesterDto requesterDto) {
        return restTemplate.postForEntity(CORE_SERVICE_URL + SLASH + ACCOUNTS + SLASH + WITHDRAW,
                new WithdrawDto(accountNumber, registrationFee, requesterDto), WithdrawResponseDto.class).getBody();
    }

    public RollBackWithdrawResponseDto callRollBack(RollbackWithdrawDto rollbackWithdrawDto) {
        return restTemplate.postForEntity(CORE_SERVICE_URL + SLASH + ACCOUNTS + SLASH + ROLLBACK_WITHDRAW,
                rollbackWithdrawDto, RollBackWithdrawResponseDto.class).getBody();
    }
}