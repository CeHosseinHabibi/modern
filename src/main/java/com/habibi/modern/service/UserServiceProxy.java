package com.habibi.modern.service;

import com.habibi.modern.client.ModernRestClient;
import com.habibi.modern.dto.UserSignUpDto;
import com.habibi.modern.entity.SignupRequest;
import com.habibi.modern.entity.UserEntity;
import com.habibi.modern.enums.ContractType;
import com.habibi.modern.enums.ErrorCode;
import com.habibi.modern.enums.RequestStatus;
import com.habibi.modern.enums.UserRole;
import com.habibi.modern.exceptions.BadRequestException;
import com.habibi.modern.exceptions.CoreInvocationException;
import com.habibi.modern.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Primary
@ConditionalOnProperty(name = "registrationFeeStrategy", havingValue = "proxy")
public class UserServiceProxy implements UserService {
    private final ModernRestClient modernRestClient;
    private final SignupRequestService signupRequestService;
    private final UserServiceImpl userServiceImpl;

    public UserEntity signUp(UserSignUpDto userSignUpDto, SignupRequest signupRequest) throws BadRequestException,
            CoreInvocationException {
        try {
            modernRestClient.callWithdraw(userSignUpDto.getAccountNumber(),
                    Utils.getRequesterDto(signupRequest.getRequesterEntity()));
            return userServiceImpl.signUp(userSignUpDto, signupRequest);
        } catch (CoreInvocationException coreInvocationException) {
            if (coreInvocationException.getErrorCode().equals(ErrorCode.CORE_IS_UNREACHABLE)) {
                signupRequest.setRequestStatus(RequestStatus.CORE_IS_UNREACHABLE);
                signupRequestService.save(signupRequest);
            }
            throw coreInvocationException;
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