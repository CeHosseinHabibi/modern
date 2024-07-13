package com.habibi.modern.service;

import com.habibi.modern.dto.UserSignUpDto;
import com.habibi.modern.entity.SignupRequest;
import com.habibi.modern.entity.UserEntity;
import com.habibi.modern.exceptions.SignUpException;

public interface UserService {
    UserEntity signUp(UserSignUpDto userSignUpDto, SignupRequest signupRequest) throws SignUpException;
}