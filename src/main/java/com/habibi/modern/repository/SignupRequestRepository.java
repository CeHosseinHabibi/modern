package com.habibi.modern.repository;

import com.habibi.modern.entity.SignupRequest;
import com.habibi.modern.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface SignupRequestRepository extends JpaRepository<SignupRequest, Long> {
    List<SignupRequest> findAllByRequestStatusInAndRequesterEntity_RequestedAtIsBetween(
            List<RequestStatus> requestStatuses, Date from, Date to);
}