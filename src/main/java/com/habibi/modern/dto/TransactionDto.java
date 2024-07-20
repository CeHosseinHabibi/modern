package com.habibi.modern.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDto {
    private Long transactionId;
    private String transactionType;
    private Long amount;
    private LocalDateTime createdAt;
    private Boolean isRollbacked;
    private Long rollbackFor;
    private String transactionStatus;
    private String description;
    private RequesterDto requesterDto;
}