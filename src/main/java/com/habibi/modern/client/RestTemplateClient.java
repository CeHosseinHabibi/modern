package com.habibi.modern.client;

import com.habibi.modern.dto.RollbackWithdrawDto;
import com.habibi.modern.dto.WithdrawDto;
import com.habibi.modern.dto.WithdrawResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateClient {
    private static final String CORE_SERVICE_URL = "http://localhost:8081";
    private static final String ACCOUNTS = "accounts";
    private static final String SLASH = "/";
    private static final String WITHDRAW = "withdraw";
    private static final String ROLLBACK_WITHDRAW = "rollback-withdraw";
    RestTemplate restTemplate = new RestTemplate();
    @Value("${core.registration.fee}")
    private Long registrationFee;

    public WithdrawResponseDto callWithdraw(Long accountNumber) {
        ResponseEntity<WithdrawResponseDto> withdrawResponseDtoResponseEntity =
                restTemplate.postForEntity(CORE_SERVICE_URL + SLASH + ACCOUNTS + SLASH + WITHDRAW,
                        new WithdrawDto(accountNumber, registrationFee), WithdrawResponseDto.class);
        return withdrawResponseDtoResponseEntity.getBody();
    }

    public void callRollBack(RollbackWithdrawDto rollbackWithdrawDto) {
        restTemplate.postForEntity(CORE_SERVICE_URL + SLASH + ACCOUNTS + SLASH + ROLLBACK_WITHDRAW,
                rollbackWithdrawDto, Void.class);
    }
}
