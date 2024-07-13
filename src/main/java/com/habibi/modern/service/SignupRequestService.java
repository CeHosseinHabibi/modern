package com.habibi.modern.service;

import com.habibi.modern.entity.SignupRequest;
import com.habibi.modern.repository.SignupRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class SignupRequestService {
    private SignupRequestRepository signupRequestRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SignupRequest save(SignupRequest signupRequest) {
        return signupRequestRepository.save(signupRequest);
    }
}