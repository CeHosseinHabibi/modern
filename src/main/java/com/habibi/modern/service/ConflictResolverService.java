package com.habibi.modern.service;

import com.habibi.modern.convertor.RequesterConvertor;
import com.habibi.modern.dto.RequesterDto;
import com.habibi.modern.dto.UserSignUpDto;
import com.habibi.modern.entity.SignupRequest;
import com.habibi.modern.enums.RequestStatus;
import com.habibi.modern.exceptions.BadRequestException;
import com.habibi.modern.repository.SignupRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ConflictResolverService {
    private final SignupRequestRepository signupRequestRepository;

    public SignupRequest saveSignUpRequest(UserSignUpDto userSignUpDto) throws BadRequestException {
        RequesterDto requesterDto = new RequesterDto(LocalDateTime.now(), userSignUpDto.getNationalCode());
        SignupRequest signupRequest = createSignupRequest(requesterDto);
        return save(signupRequest);
    }

    private SignupRequest save(SignupRequest signupRequest) throws BadRequestException {
        try {
            return signupRequestRepository.save(signupRequest);
        } catch (DataIntegrityViolationException exception) {
            throw new BadRequestException(exception.getMessage());
        }
    }

    private SignupRequest createSignupRequest(RequesterDto requesterDto) {
        SignupRequest signupRequest = SignupRequest.builder().requesterEntity(
                RequesterConvertor.getRequesterEntity(requesterDto)).requestStatus(RequestStatus.CREATED).build();
        return signupRequest;
    }
}