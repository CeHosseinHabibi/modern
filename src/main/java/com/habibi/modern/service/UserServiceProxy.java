package com.habibi.modern.service;

import com.habibi.modern.client.RestTemplateClient;
import com.habibi.modern.dto.UserSignUpDto;
import com.habibi.modern.dto.WithdrawResponseDto;
import com.habibi.modern.entity.SignupRequest;
import com.habibi.modern.entity.UserEntity;
import com.habibi.modern.enums.ContractType;
import com.habibi.modern.enums.ErrorCode;
import com.habibi.modern.enums.RequestStatus;
import com.habibi.modern.enums.UserRole;
import com.habibi.modern.exceptions.SignUpException;
import com.habibi.modern.exceptions.SignUpWithdrawException;
import com.habibi.modern.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Primary
@ConditionalOnProperty(name = "registrationFeeStrategy", havingValue = "proxy")
public class UserServiceProxy implements UserService {
    private final RestTemplateClient restTemplateClient;
    private final SignupRequestService signupRequestService;
    private final UserServiceImpl userServiceImpl;

    public UserEntity signUp(UserSignUpDto userSignUpDto, SignupRequest signupRequest) throws SignUpException {
        try {
            restTemplateClient.callWithdraw(userSignUpDto.getAccountNumber(),
                    Utils.getRequesterDto(signupRequest.getRequesterEntity()));
            return userServiceImpl.signUp(userSignUpDto, signupRequest);
        } catch (ResourceAccessException resourceAccessException) {
            signupRequest.setRequestStatus(RequestStatus.CORE_IS_UNREACHABLE);
            signupRequestService.save(signupRequest);
            throw new SignUpWithdrawException(ErrorCode.CORE_IS_UNREACHABLE,
                    "A connection problem with core system");
        } catch (HttpClientErrorException httpClientErrorException) {
            WithdrawResponseDto exceptionBody = httpClientErrorException.getResponseBodyAs(WithdrawResponseDto.class);
            throw new SignUpWithdrawException(exceptionBody.getErrorCode(), exceptionBody.getDescription());
        } catch (HttpServerErrorException httpServerErrorException) {
            throw new SignUpWithdrawException(ErrorCode.CORE_THROWS_INTERNAL_SERVER_ERROR,
                    "Core system -> withdraw returned 500 statues code.");
        }
    }

    @Override
    public Page<UserEntity> search(String username, String firstName, String lastName, LocalDateTime createdFrom,
                                   LocalDateTime createdTo, String nationalCode, String organizationName,
                                   ContractType contractType, UserRole userRole, Long cif, int page, int size,
                                   String sortBy) {
        return userServiceImpl.search(username, firstName, lastName, createdFrom, createdTo, nationalCode,
                organizationName, contractType, userRole, cif, page, size, sortBy);
    }
}