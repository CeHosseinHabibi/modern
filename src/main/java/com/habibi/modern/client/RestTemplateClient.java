//package com.habibi.modern.client;
//
//import com.habibi.modern.dto.*;
//import com.habibi.modern.enums.ErrorCode;
//import com.habibi.modern.exceptions.CoreInvocationException;
//import com.habibi.modern.exceptions.TosanException;
//import com.habibi.modern.exceptions.corresponding.ModernInsufficientFundsException;
//import lombok.RequiredArgsConstructor;
//import lombok.SneakyThrows;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.HttpServerErrorException;
//import org.springframework.web.client.ResourceAccessException;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//@RequiredArgsConstructor
//@ConditionalOnProperty(name = "rest.service", havingValue = "rest-template")
//public class RestTemplateClient implements ModernRestClient {
//
//    private final RestTemplate restTemplate;
//    @Value("${core.registration.fee}")
//    private Long registrationFee;
//    @Value("${microservice.core.url}")
//    private String microserviceCoreURL;
//    @Value("${microservice.core.withdraw.url}")
//    private String microserviceCoreWithdrawURL;
//    @Value("${microservice.core.rollback.withdraw.url}")
//    private String microserviceCoreRollbackWithdrawURL;
//
//    public WithdrawResponseDto callWithdraw(Long accountNumber, RequesterDto requesterDto)
//            throws CoreInvocationException, ModernInsufficientFundsException {
//        try {
//            return restTemplate.postForEntity(microserviceCoreURL + microserviceCoreWithdrawURL,
//                    new WithdrawDto(accountNumber, registrationFee, requesterDto), WithdrawResponseDto.class).getBody();
//        } catch (ResourceAccessException resourceAccessException) {
//            throw new CoreInvocationException(ErrorCode.CORE_IS_UNREACHABLE);
//        } catch (HttpClientErrorException httpClientErrorException) {
//            ErrorDto errorDto = httpClientErrorException.getResponseBodyAs(ErrorDto.class);
//            String correspondingModernException = "com.habibi.modern.exceptions.Modern" + errorDto.getErrorType();
//            Exception ex;
//            try {
//                ex =  ((Exception) Class.forName(correspondingModernException).getDeclaredConstructor(ErrorDto.class).newInstance(errorDto));
//            } catch (Exception e) {
//                throw e;
//            }
//            throw ex;
//        } catch (HttpServerErrorException httpServerErrorException) {
//            throw new CoreInvocationException(ErrorCode.CORE_THROWS_INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    public RollBackWithdrawResponseDto callRollBack(RollbackWithdrawDto rollbackWithdrawDto)
//            throws CoreInvocationException {
//        try {
//            return restTemplate.postForEntity(microserviceCoreURL + microserviceCoreRollbackWithdrawURL, rollbackWithdrawDto,
//                    RollBackWithdrawResponseDto.class).getBody();
//        } catch (ResourceAccessException resourceAccessException) {
//            throw new CoreInvocationException(ErrorCode.CORE_IS_UNREACHABLE);
//        } catch (HttpClientErrorException httpClientErrorException) {
//            throw httpClientErrorException.getResponseBodyAs(CoreInvocationException.class);
//        } catch (HttpServerErrorException httpServerErrorException) {
//            throw new CoreInvocationException(ErrorCode.CORE_THROWS_INTERNAL_SERVER_ERROR);
//        }
//    }
//}
