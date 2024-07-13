package com.habibi.modern.configuration;


import com.habibi.modern.client.RestTemplateClient;
import com.habibi.modern.dto.RequesterDto;
import com.habibi.modern.dto.RollBackWithdrawResponseDto;
import com.habibi.modern.dto.RollbackWithdrawDto;
import com.habibi.modern.entity.RequesterEntity;
import com.habibi.modern.entity.SignupRequest;
import com.habibi.modern.enums.ErrorCode;
import com.habibi.modern.enums.RequestStatus;
import com.habibi.modern.repository.SignupRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
@AllArgsConstructor
public class ConflictResolverScheduler {
    private SignupRequestRepository signupRequestRepository;
    private RestTemplateClient restTemplateClient;

    @Scheduled(fixedDelay = 2, timeUnit = TimeUnit.HOURS)
    public void resolveConflicts() {
        Date now = new Date();
        Instant scannableTo = now.toInstant().minus(15, TimeUnit.MINUTES.toChronoUnit());
        Instant scannableFrom = scannableTo.minus(3, TimeUnit.DAYS.toChronoUnit());
        List<RequestStatus> potentialConflictsStatuses =
                List.of(RequestStatus.CREATED, RequestStatus.CORE_IS_UNREACHABLE);

        List<SignupRequest> potentialConflicts =
                signupRequestRepository.findAllByRequestStatusInAndRequesterEntity_RequestedAtIsBetween(
                        potentialConflictsStatuses, Date.from(scannableFrom), Date.from(scannableTo));

        if (potentialConflicts.isEmpty())
            return;

        for (SignupRequest potentialConflict : potentialConflicts) {
            try {
                RequesterEntity requester = potentialConflict.getRequesterEntity();
                RollbackWithdrawDto rollbackDto = new RollbackWithdrawDto(
                        new RequesterDto(requester.getRequestedAt(), requester.getUserNationalCode()));

                potentialConflict.setLastRollbackTryDate(new Date());
                restTemplateClient.callRollBack(rollbackDto);
                potentialConflict.setRequestStatus(RequestStatus.RESOLVED_CONFLICT_BY_ROLLBACKING_ITS_WITHDRAW_TRANSACTION);
            } catch (ResourceAccessException resourceAccessException) {
                potentialConflict.setLastRollbackTryErrorCode(ErrorCode.CORE_IS_UNREACHABLE);
            } catch (HttpClientErrorException httpClientErrorException) {
                RollBackWithdrawResponseDto exceptionBody =
                        httpClientErrorException.getResponseBodyAs(RollBackWithdrawResponseDto.class);
                potentialConflict.setLastRollbackTryErrorCode(exceptionBody.getErrorCode());
                potentialConflict.setLastRollbackTryDescription(exceptionBody.getDescription());
                potentialConflict.setRequestStatus(RequestStatus.RESOLVED_CONFLICT_CONTAINS_TRANSACTION_HAS_BAD_REQUEST_ERROR);
            } catch (HttpServerErrorException httpServerErrorException) {
                potentialConflict.setLastRollbackTryErrorCode(ErrorCode.CORE_THROWS_INTERNAL_SERVER_ERROR);
            } finally {
                signupRequestRepository.save(potentialConflict);
            }
        }

    }
}