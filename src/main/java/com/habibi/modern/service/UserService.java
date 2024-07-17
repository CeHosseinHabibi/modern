package com.habibi.modern.service;

import com.habibi.modern.dto.UserSignUpDto;
import com.habibi.modern.entity.SignupRequest;
import com.habibi.modern.entity.UserEntity;
import com.habibi.modern.enums.ContractType;
import com.habibi.modern.enums.UserRole;
import com.habibi.modern.exceptions.SignUpException;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface UserService {
    UserEntity signUp(UserSignUpDto userSignUpDto, SignupRequest signupRequest) throws SignUpException;

    Page<UserEntity> search(String username, String firstName, String lastName, LocalDateTime createdFrom,
                            LocalDateTime createdTo, String nationalCode, String organizationName,
                            ContractType contractType, UserRole userRole, Long cif, int page, int size, String sortBy);
}