package com.habibi.modern.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.habibi.modern.dto.ErrorDto;
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
            return new CoreInvocationException(ErrorCode.CORE_THROWS_INTERNAL_SERVER_ERROR);
        }

        String body = Util.toString(response.body().asReader());
        ErrorDto errorDto = objectMapper.readValue(body, ErrorDto.class);

        String correspondingModernException = "com.habibi.modern.exceptions.corecorresponding.Modern" + errorDto.getErrorType();
        Exception exception = ((Exception) Class.forName(correspondingModernException)
                .getDeclaredConstructor(ErrorDto.class).newInstance(errorDto));
        return exception;
    }
}
