package com.habibi.modern.service;

import com.habibi.modern.dto.UserSignUpDto;
import com.habibi.modern.entity.BankUser;
import com.habibi.modern.entity.ThirdPartyUser;
import com.habibi.modern.entity.UserEntity;
import com.habibi.modern.exceptions.SignUpException;
import com.habibi.modern.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    public UserEntity signUp(UserSignUpDto userSignUpDto) throws SignUpException {
        validate(userSignUpDto);
        UserEntity userEntity = createUser(userSignUpDto);
        return userRepository.save(userEntity);
    }

    public UserEntity createUser(UserSignUpDto userSignUpDto) {
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
        return userEntity;
    }

    public ThirdPartyUser createThirdPartyUser(UserSignUpDto userSignUpDto) {
        return ThirdPartyUser.builder()
                .organizationName(userSignUpDto.getOrganizationName())
                .contractType(userSignUpDto.getContractType())
                .userRole(userSignUpDto.getUserRole())
                .build();
    }

    public BankUser createBankUser() {
        return new BankUser();
    }

    public void validate(UserSignUpDto userSignUpDto) throws SignUpException {
        if (userRepository.findUserEntityByUsername(userSignUpDto.getUsername()).isPresent())
            throw new SignUpException("Username is duplicated.");
    }

}