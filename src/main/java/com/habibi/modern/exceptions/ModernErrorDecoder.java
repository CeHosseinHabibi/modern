package com.habibi.modern.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.habibi.modern.dto.RollBackWithdrawResponseDto;
import com.habibi.modern.dto.WithdrawResponseDto;
import com.habibi.modern.enums.ErrorCode;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;

public class ModernErrorDecoder implements ErrorDecoder {
    private ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @Override
    public Exception decode(String methodSignature, Response response) {
        int status = response.status();
        if (status == 500) {
            if (methodSignature.contains("callWithdraw")) {
                return new SignUpWithdrawException(ErrorCode.CORE_THROWS_INTERNAL_SERVER_ERROR,
                        "Core system -> withdraw returned 500 status code.");
            } else if (methodSignature.contains("callRollBack")) {
                return new RollbackWithdrawException(ErrorCode.CORE_THROWS_INTERNAL_SERVER_ERROR,
                        "Core system -> rollback-withdraw returned 500 status code.");
            }
        } else if (status == 400) {
            String body = Util.toString(response.body().asReader());
            if (methodSignature.contains("callWithdraw")) {
                WithdrawResponseDto exception = objectMapper.readValue(body, WithdrawResponseDto.class);
                return new SignUpWithdrawException(exception.getErrorCode(), exception.getDescription());
            } else if (methodSignature.contains("callRollBack")) {
                RollBackWithdrawResponseDto exception = objectMapper.readValue(body, RollBackWithdrawResponseDto.class);
                return new RollbackWithdrawException(exception.getErrorCode(), exception.getDescription());
            }
        }

        return new Exception("");
    }
}
