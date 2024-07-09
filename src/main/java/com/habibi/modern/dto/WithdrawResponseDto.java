package com.habibi.modern.dto;

import com.habibi.modern.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawResponseDto {
    private TransactionDto transactionDto;
    private ErrorCode errorCode;
    private String description;
}