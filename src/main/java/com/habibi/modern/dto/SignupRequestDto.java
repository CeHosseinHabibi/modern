package com.habibi.modern.dto;

import com.habibi.modern.enums.ErrorCode;
import com.habibi.modern.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {
    private Long id;
    private RequestStatus requestStatus;
    private ErrorCode lastRollbackTryErrorCode;
    private String lastRollbackTryDescription;
    private LocalDateTime lastRollbackTryDate;
    private RequesterDto requesterDto;
}