package com.habibi.modern.service;

import com.habibi.modern.dto.RequesterDto;
import com.habibi.modern.dto.UserSignUpDto;
import com.habibi.modern.entity.SignupRequest;
import com.habibi.modern.enums.RequestStatus;
import com.habibi.modern.exceptions.SignUpException;
import com.habibi.modern.repository.SignupRequestRepository;
import com.habibi.modern.util.Utils;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ConflictResolverService {
    private final SignupRequestRepository signupRequestRepository;

    public SignupRequest saveSignUpRequest(UserSignUpDto userSignUpDto) throws SignUpException {
        RequesterDto requesterDto = new RequesterDto(LocalDateTime.now(), userSignUpDto.getNationalCode());
        SignupRequest signupRequest = createSignupRequest(requesterDto);
        return save(signupRequest);
    }

    private SignupRequest save(SignupRequest signupRequest) throws SignUpException {
        try {
            return signupRequestRepository.save(signupRequest);
        } catch (DataIntegrityViolationException exception) {
            throw new SignUpException(exception.getMessage());
        }
    }

    private SignupRequest createSignupRequest(RequesterDto requesterDto) {
        SignupRequest signupRequest = SignupRequest.builder().requesterEntity(Utils.getRequesterEntity(requesterDto))
                .requestStatus(RequestStatus.CREATED).build();
        return signupRequest;
    }
}