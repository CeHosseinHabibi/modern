package com.habibi.modern.aop;

import com.habibi.modern.client.RestTemplateClient;
import com.habibi.modern.dto.UserSignUpDto;
import com.habibi.modern.dto.WithdrawResponseDto;
import com.habibi.modern.entity.SignupRequest;
import com.habibi.modern.enums.ErrorCode;
import com.habibi.modern.enums.RequestStatus;
import com.habibi.modern.exceptions.SignUpWithdrawException;
import com.habibi.modern.service.SignupRequestService;
import com.habibi.modern.util.Utils;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

@Aspect
@Component
@AllArgsConstructor
@ConditionalOnProperty(name = "registrationFeeStrategy", havingValue = "aspect")
public class RegistrationFeeAspect {
    private final RestTemplateClient restTemplateClient;
    private final SignupRequestService signupRequestService;

    @Pointcut("execution(public * com.habibi.modern.service.UserServiceImpl.signUp(..))")
    private void UserServiceImpl_signUpMethod() {
    }

    @Before(value = "UserServiceImpl_signUpMethod()")
    public void payRegistrationFeeBeforeUserSignup(JoinPoint joinPoint) throws SignUpWithdrawException {
        Object[] args = joinPoint.getArgs();
        UserSignUpDto userSignUpDto = (UserSignUpDto) args[0];
        SignupRequest signupRequest = (SignupRequest) args[1];

        try {
            restTemplateClient.callWithdraw(userSignUpDto.getAccountNumber(),
                    Utils.getRequesterDto(signupRequest.getRequesterEntity()));
        } catch (ResourceAccessException resourceAccessException) {
            signupRequest.setRequestStatus(RequestStatus.TIME_OUT_OR_UNREACHABLE_CORE);
            signupRequestService.save(signupRequest);
            throw new SignUpWithdrawException(ErrorCode.TIME_OUT_OR_UNREACHABLE_CORE, "A connection problem with core system");
        } catch (HttpClientErrorException httpClientErrorException) {
            WithdrawResponseDto exceptionBody = httpClientErrorException.getResponseBodyAs(WithdrawResponseDto.class);
            throw new SignUpWithdrawException(exceptionBody.getErrorCode(), exceptionBody.getDescription());
        } catch (HttpServerErrorException httpServerErrorException) {
            throw new SignUpWithdrawException(ErrorCode.CORE_THROWS_500, "Core system -> withdraw returned 500 statues code.");
        }
    }
}