package com.habibi.modern.service;

import com.habibi.modern.client.RestTemplateClient;
import com.habibi.modern.dto.RollbackWithdrawDto;
import com.habibi.modern.dto.UserSignUpDto;
import com.habibi.modern.dto.WithdrawResponseDto;
import com.habibi.modern.entity.BankUser;
import com.habibi.modern.entity.ThirdPartyUser;
import com.habibi.modern.entity.UserEntity;
import com.habibi.modern.enums.ErrorCode;
import com.habibi.modern.exceptions.SignUpException;
import com.habibi.modern.exceptions.SignUpWithdrawException;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

@Service
@RequiredArgsConstructor
@Primary
public class UserServiceProxy implements UserService {
    private final RestTemplateClient restTemplateClient;
    private final UserServiceImpl userServiceImpl;
    private static final Logger logger = LogManager.getLogger(UserServiceProxy.class);

    public UserEntity signUp(UserSignUpDto userSignUpDto) throws SignUpWithdrawException {
        WithdrawResponseDto withdrawResponseDto = null;
        try {
            withdrawResponseDto = restTemplateClient.callWithdraw(userSignUpDto.getAccountNumber());
            return userServiceImpl.signUp(userSignUpDto);
        } catch (ResourceAccessException resourceAccessException) {
            logger.error(resourceAccessException);
            throw new SignUpWithdrawException(ErrorCode.CORE_CONNECTION_REFUSED, "Core system -> Connection refused.");
        } catch (HttpClientErrorException httpClientErrorException) {
            logger.error(httpClientErrorException);
            WithdrawResponseDto exceptionBody = httpClientErrorException.getResponseBodyAs(WithdrawResponseDto.class);
            throw new SignUpWithdrawException(exceptionBody.getErrorCode(), exceptionBody.getDescription());
        } catch (HttpServerErrorException httpServerErrorException) {
            logger.error(httpServerErrorException);
            throw new SignUpWithdrawException(ErrorCode.CORE_THROWS_500, "Core system -> withdraw returned 500 statues code.");
        } catch (SignUpException signUpException) {
            logger.error(signUpException);
            restTemplateClient.callRollBack(new RollbackWithdrawDto(withdrawResponseDto.getTrackingCode()));
        } catch (Exception exception) {
            logger.error(exception);
            restTemplateClient.callRollBack(new RollbackWithdrawDto(withdrawResponseDto.getTrackingCode()));
        }
        return null;
//        1 ->connection refused        =>return connection time out with core
//        1 ->400                       =>return 400 exception to client
//        1 ->500                       =>return 500 exception to client
//        1 ->200, timeout for response =>check request by giving request - id and receiving trackingCode. if trackingCode is not - 1, call userServiceImpl.signUp(userSignUpDto);
//        1 ->200, 2 ->400              =>coreClient.rollbackWithdraw(new RollbackWithdrawDto(withdrawResponseDto.getTrackingCode()));
//        1 ->200, 2 ->500              =>coreClient.rollbackWithdraw(new RollbackWithdrawDto(withdrawResponseDto.getTrackingCode()));
//        1 ->200, 2 ->200              =>nothing
    }

    @Override
    public UserEntity createUser(UserSignUpDto userSignUpDto) {
        return userServiceImpl.createUser(userSignUpDto);
    }

    @Override
    public ThirdPartyUser createThirdPartyUser(UserSignUpDto userSignUpDto) {
        return userServiceImpl.createThirdPartyUser(userSignUpDto);
    }

    @Override
    public BankUser createBankUser() {
        return userServiceImpl.createBankUser();
    }

    @Override
    public void validate(UserSignUpDto userSignUpDto) throws SignUpException {
        userServiceImpl.validate(userSignUpDto);
    }

}