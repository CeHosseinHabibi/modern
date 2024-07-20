package com.habibi.modern.service;

import com.habibi.modern.entity.SignupRequest;
import com.habibi.modern.repository.SignupRequestRepository;
import com.habibi.modern.specification.SignupRequestSpecifications;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class SignupRequestService {
    private SignupRequestRepository signupRequestRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SignupRequest save(SignupRequest signupRequest) {
        return signupRequestRepository.save(signupRequest);
    }

    public Page<SignupRequest> getConflicts(LocalDateTime createdFrom, LocalDateTime createdTo,
                                            int page, int size, String sortBy) {
        Pageable pageRequest = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Specification<SignupRequest> signupRequestSpecification = Specification
                .where(SignupRequestSpecifications.rollbackableSignupRequests())
                .and(SignupRequestSpecifications.createdFrom(createdFrom))
                .and(SignupRequestSpecifications.createdTo(createdTo));
        return signupRequestRepository.findAll(signupRequestSpecification, pageRequest);

    }
}