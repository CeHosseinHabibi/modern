package com.habibi.modern.service;

import com.habibi.modern.dto.UserSignUpDto;
import com.habibi.modern.entity.BankUser;
import com.habibi.modern.entity.SignupRequest;
import com.habibi.modern.entity.ThirdPartyUser;
import com.habibi.modern.entity.UserEntity;
import com.habibi.modern.enums.RequestStatus;
import com.habibi.modern.exceptions.SignUpException;
import com.habibi.modern.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Transactional(rollbackFor = SignUpException.class)
    public UserEntity signUp(UserSignUpDto userSignUpDto, SignupRequest signupRequest) throws SignUpException {
        signupRequest.setRequestStatus(RequestStatus.SIGNUP_DONE);
        validate(userSignUpDto);
        UserEntity userEntity = createUser(userSignUpDto);
        return saveUser(userEntity);
    }

    private UserEntity saveUser(UserEntity userEntity) throws SignUpException {
        try {
            return userRepository.save(userEntity);
        } catch (DataIntegrityViolationException exception) {
            throw new SignUpException(exception.getMessage());
        }
    }

    private UserEntity createUser(UserSignUpDto userSignUpDto) {
        UserEntity userEntity = null;
        switch (userSignUpDto.getUserType()) {
            case BANKING:
                userEntity = this.createBankUser();
                break;
            case THIRD_PARTY:
                userEntity = this.createThirdPartyUser(userSignUpDto);
                break;
        }
        userEntity.setUsername(userSignUpDto.getUsername());
        userEntity.setPassword(userSignUpDto.getPassword());
        userEntity.setFirstName(userSignUpDto.getFirstName());
        userEntity.setLastName(userSignUpDto.getLastName());
        userEntity.setNationalCode(userSignUpDto.getNationalCode());
        return userEntity;
    }

    private ThirdPartyUser createThirdPartyUser(UserSignUpDto userSignUpDto) {
        return ThirdPartyUser.builder()
                .organizationName(userSignUpDto.getOrganizationName())
                .contractType(userSignUpDto.getContractType())
                .userRole(userSignUpDto.getUserRole())
                .build();
    }

    private BankUser createBankUser() {
        return new BankUser();
    }

    private void validate(UserSignUpDto userSignUpDto) throws SignUpException {
        if (userRepository.findUserEntityByNationalCode(userSignUpDto.getNationalCode()).isPresent())
            throw new SignUpException("User with the national code has already been registered.");

        if (userRepository.findUserEntityByUsername(userSignUpDto.getUsername()).isPresent())
            throw new SignUpException("Username is duplicated.");
    }

}