package com.habibi.modern.service;

import com.habibi.modern.client.CoreClient;
import com.habibi.modern.dto.RollbackWithdrawDto;
import com.habibi.modern.dto.UserSignUpDto;
import com.habibi.modern.dto.WithdrawDto;
import com.habibi.modern.dto.WithdrawResponseDto;
import feign.Feign;
import feign.FeignException;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceProxy {
    private static final String CORE_SERVICE_URL = "http://localhost:8081";
    private static final String ACCOUNT_END_POINT_URL = "accounts";
    private static final String SLASH = "/";
    private final UserService userService;
    private CoreClient coreClient = Feign.builder().client(new OkHttpClient()).encoder(new GsonEncoder())
            .decoder(new GsonDecoder()).target(CoreClient.class, CORE_SERVICE_URL + SLASH + ACCOUNT_END_POINT_URL);
    @Transactional
    public void signUp(UserSignUpDto userSignUpDto){
        WithdrawResponseDto withdrawResponseDto = null;
        try{
            withdrawResponseDto = coreClient.withdraw(new WithdrawDto(1L,100L));
            userService.signUp(userSignUpDto);
        }catch (FeignException feignException){
            coreClient.rollbackWithdraw(new RollbackWithdrawDto(withdrawResponseDto.getTrackingCode()));

            //1 -> connection refused           => retry 1 (3 times) and then return connection time out with core
            //1 -> 400                          => return 400 exception to client
            //1 -> 500                          => return 500 exception to client
            //1 -> 200, timeout for response    =>
            //1 -> 200, 2 -> 400                => coreClient.rollbackWithdraw(new RollbackWithdrawDto(withdrawResponseDto.getTrackingCode()));
            //1 -> 200, 2 -> 500                => coreClient.rollbackWithdraw(new RollbackWithdrawDto(withdrawResponseDto.getTrackingCode()));
            //1 -> 200, 2 -> 200                => nothing

        }
    }

    public boolean isValid(UserSignUpDto userSignUpDto) {
        return userService.isValid(userSignUpDto);
    }
}