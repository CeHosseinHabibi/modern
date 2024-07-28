package com.habibi.modern.configuration;


import com.habibi.modern.client.ModernRestClient;
import com.habibi.modern.dto.RequesterDto;
import com.habibi.modern.dto.RollbackWithdrawDto;
import com.habibi.modern.entity.RequesterEntity;
import com.habibi.modern.entity.SignupRequest;
import com.habibi.modern.enums.ErrorCode;
import com.habibi.modern.enums.RequestStatus;
import com.habibi.modern.exceptions.RollbackWithdrawException;
import com.habibi.modern.repository.SignupRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableScheduling
@AllArgsConstructor
public class ConflictResolverScheduler {
    private SignupRequestRepository signupRequestRepository;
    private ModernRestClient modernRestClient;

    @Scheduled(cron = "0 15 2 * * ?")
    public void resolveConflicts() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime scannableTo = now.minusMinutes(15);
        LocalDateTime scannableFrom = scannableTo.minusDays(1);
        List<RequestStatus> potentialConflictsStatuses =
                List.of(RequestStatus.CREATED, RequestStatus.CORE_IS_UNREACHABLE);

        List<SignupRequest> potentialConflicts =
                signupRequestRepository.findAllByRequestStatusInAndRequesterEntity_RequestedAtIsBetween(
                        potentialConflictsStatuses, scannableFrom, scannableTo);

        if (potentialConflicts.isEmpty())
            return;

        for (SignupRequest potentialConflict : potentialConflicts) {
            try {
                RequesterEntity requester = potentialConflict.getRequesterEntity();
                RollbackWithdrawDto rollbackDto = new RollbackWithdrawDto(
                        new RequesterDto(requester.getRequestedAt(), requester.getUserNationalCode()));
                potentialConflict.setLastRollbackTryDate(LocalDateTime.now());
                modernRestClient.callRollBack(rollbackDto);
                potentialConflict.setRequestStatus(RequestStatus.RESOLVED_CONFLICT_BY_ROLLBACKING_ITS_WITHDRAW_TRANSACTION);
            } catch (RollbackWithdrawException rollbackWithdrawException) {
                potentialConflict.setLastRollbackTryErrorCode(rollbackWithdrawException.getErrorCode());
                potentialConflict.setLastRollbackTryDescription(rollbackWithdrawException.getMessage());
                if (!rollbackWithdrawException.getErrorCode().equals(ErrorCode.CORE_IS_UNREACHABLE) &&
                        !rollbackWithdrawException.getErrorCode().equals(ErrorCode.CORE_THROWS_INTERNAL_SERVER_ERROR)) {
                    potentialConflict.setRequestStatus(RequestStatus.RESOLVED_CONFLICT_CONTAINS_TRANSACTION_HAS_BAD_REQUEST_ERROR);
                }
            } finally {
                signupRequestRepository.save(potentialConflict);
            }
        }

    }
}