package com.habibi.modern.aop;

import com.habibi.modern.client.ModernRestClient;
import com.habibi.modern.dto.UserSignUpDto;
import com.habibi.modern.entity.SignupRequest;
import com.habibi.modern.enums.ErrorCode;
import com.habibi.modern.enums.RequestStatus;
import com.habibi.modern.exceptions.CoreInvocationException;
import com.habibi.modern.service.SignupRequestService;
import com.habibi.modern.util.Utils;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
@ConditionalOnProperty(name = "registrationFeeStrategy", havingValue = "aspect")
public class RegistrationFeeAspect {
    private final ModernRestClient modernRestClient;
    private final SignupRequestService signupRequestService;

    @Pointcut("execution(public * com.habibi.modern.service.UserServiceImpl.signUp(..))")
    private void UserServiceImpl_signUpMethod() {
    }

    @Before(value = "UserServiceImpl_signUpMethod()")
    public void payRegistrationFeeBeforeUserSignup(JoinPoint joinPoint) throws CoreInvocationException {
        Object[] args = joinPoint.getArgs();
        UserSignUpDto userSignUpDto = (UserSignUpDto) args[0];
        SignupRequest signupRequest = (SignupRequest) args[1];

        try {
            modernRestClient.callWithdraw(userSignUpDto.getAccountNumber(),
                    Utils.getRequesterDto(signupRequest.getRequesterEntity()));
        } catch (CoreInvocationException coreInvocationException) {
            if (coreInvocationException.getErrorCode().equals(ErrorCode.CORE_IS_UNREACHABLE)) {
                signupRequest.setRequestStatus(RequestStatus.CORE_IS_UNREACHABLE);
                signupRequestService.save(signupRequest);
            }
            throw coreInvocationException;
        }
    }
}