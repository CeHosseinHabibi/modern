package com.habibi.modern.service;

import com.habibi.modern.dto.UserSignUpDto;
import com.habibi.modern.entity.SignupRequest;
import com.habibi.modern.entity.UserEntity;
import com.habibi.modern.enums.RequestStatus;
import com.habibi.modern.exceptions.SignUpException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionalUserServiceImpl {
    private final UserServiceImpl userServiceImpl;

    @Transactional(rollbackFor = SignUpException.class)
    public UserEntity transactionalSignup(UserSignUpDto userSignUpDto, SignupRequest signupRequest)
            throws SignUpException {
        try {
            signupRequest.setRequestStatus(RequestStatus.SIGNUP_DONE);
            return userServiceImpl.signUp(userSignUpDto);
        } catch (Exception exception) {
            throw new SignUpException(exception.getMessage());
        }
    }
}