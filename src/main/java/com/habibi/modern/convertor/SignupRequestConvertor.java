package com.habibi.modern.convertor;

import com.habibi.modern.dto.SignupRequestDto;
import com.habibi.modern.entity.SignupRequest;

public class SignupRequestConvertor {
    public static SignupRequestDto toSignupRequestDto(SignupRequest signupRequest) {
        return signupRequest == null ? null : new SignupRequestDto(signupRequest.getId(),
                signupRequest.getRequestStatus(), signupRequest.getLastRollbackTryErrorCode(),
                signupRequest.getLastRollbackTryDescription(), signupRequest.getLastRollbackTryDate(),
                RequesterConvertor.getRequesterDto(signupRequest.getRequesterEntity()));
    }
}