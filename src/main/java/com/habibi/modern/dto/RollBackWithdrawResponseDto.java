package com.habibi.modern.dto;

import com.habibi.modern.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RollBackWithdrawResponseDto {
    private TransactionDto transactionDto;
}