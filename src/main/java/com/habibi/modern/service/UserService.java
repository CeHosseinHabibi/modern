package com.habibi.modern.service;

import com.habibi.modern.entity.BankUser;
import com.habibi.modern.entity.ThirdPartyUser;
import com.habibi.modern.dto.UserSignUpDto;
import com.habibi.modern.entity.UserEntity;
import com.habibi.modern.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;

    @Transactional
    public void signUp(UserSignUpDto userSignUpDto){
        if (!isValid(userSignUpDto))
            return;    //throw an exception

        UserEntity userEntity = createUser(userSignUpDto);
        userRepository.save(userEntity);
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

    public boolean isValid(UserSignUpDto userSignUpDto) {
        return true; //ToDo implement the logic
    }

}