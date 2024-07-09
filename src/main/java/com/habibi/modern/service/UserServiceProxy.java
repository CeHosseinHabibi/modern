package com.habibi.modern.service;

import com.habibi.modern.client.RestTemplateClient;
import com.habibi.modern.dto.RequesterDto;
import com.habibi.modern.dto.UserSignUpDto;
import com.habibi.modern.dto.WithdrawResponseDto;
import com.habibi.modern.entity.RequesterEntity;
import com.habibi.modern.entity.SignupRequest;
import com.habibi.modern.entity.UserEntity;
import com.habibi.modern.enums.ErrorCode;
import com.habibi.modern.enums.RequestStatus;
import com.habibi.modern.exceptions.SignUpException;
import com.habibi.modern.exceptions.SignUpWithdrawException;
import com.habibi.modern.repository.SignupRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Primary
public class UserServiceProxy implements UserService {
    private final RestTemplateClient restTemplateClient;
    private final TransactionalUserServiceImpl transactionalUserServiceImpl;
    private final SignupRequestRepository signupRequestRepository;

    public UserEntity signUp(UserSignUpDto userSignUpDto) throws SignUpException {
        RequesterDto requesterDto = new RequesterDto(new Date(), userSignUpDto.getNationalCode());
        SignupRequest signupRequest = createSignupRequest(requesterDto);
        saveSignupRequest(signupRequest);

        try {
            restTemplateClient.callWithdraw(userSignUpDto.getAccountNumber(), requesterDto);
            return transactionalUserServiceImpl.transactionalSignup(userSignUpDto, signupRequest);
        } catch (ResourceAccessException resourceAccessException) {
            signupRequest.setRequestStatus(RequestStatus.TIME_OUT_OR_UNREACHABLE_CORE);
            signupRequestRepository.save(signupRequest);
            throw new SignUpWithdrawException(ErrorCode.TIME_OUT_OR_UNREACHABLE_CORE, "A connection problem with core system");
        } catch (HttpClientErrorException httpClientErrorException) {
            WithdrawResponseDto exceptionBody = httpClientErrorException.getResponseBodyAs(WithdrawResponseDto.class);
            throw new SignUpWithdrawException(exceptionBody.getErrorCode(), exceptionBody.getDescription());
        } catch (HttpServerErrorException httpServerErrorException) {
            throw new SignUpWithdrawException(ErrorCode.CORE_THROWS_500, "Core system -> withdraw returned 500 statues code.");
        }
    }

    private void saveSignupRequest(SignupRequest signupRequest) throws SignUpException {
        try {
            signupRequestRepository.save(signupRequest);
        } catch (DataIntegrityViolationException exception) {
            throw new SignUpException(exception.getMessage());
        }
    }

    private SignupRequest createSignupRequest(RequesterDto requesterDto) {
        SignupRequest signupRequest = SignupRequest.builder()
                .requesterEntity(new RequesterEntity(requesterDto.getRequestedAt(), requesterDto.getUserNationalCode()))
                .requestStatus(RequestStatus.CREATED)
                .build();
        return signupRequest;
    }
}