package com.habibi.modern.repository;

import com.habibi.modern.entity.SignupRequest;
import com.habibi.modern.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface SignupRequestRepository extends JpaRepository<SignupRequest, Long>, JpaSpecificationExecutor {
    List<SignupRequest> findAllByRequestStatusInAndRequesterEntity_RequestedAtIsBetween(
            List<RequestStatus> requestStatuses, LocalDateTime from, LocalDateTime to);
}