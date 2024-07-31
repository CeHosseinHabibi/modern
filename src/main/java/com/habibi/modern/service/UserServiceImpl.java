package com.habibi.modern.service;

import com.habibi.modern.dto.UserSignUpDto;
import com.habibi.modern.entity.BankUser;
import com.habibi.modern.entity.SignupRequest;
import com.habibi.modern.entity.ThirdPartyUser;
import com.habibi.modern.entity.UserEntity;
import com.habibi.modern.enums.ContractType;
import com.habibi.modern.enums.ErrorCode;
import com.habibi.modern.enums.RequestStatus;
import com.habibi.modern.enums.UserRole;
import com.habibi.modern.exceptions.BadRequestException;
import com.habibi.modern.repository.UserRepository;
import com.habibi.modern.specification.UserSpecifications;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Transactional(rollbackFor = BadRequestException.class)
    public UserEntity signUp(UserSignUpDto userSignUpDto, SignupRequest signupRequest) throws BadRequestException {
        signupRequest.setRequestStatus(RequestStatus.SIGNUP_DONE);
        validate(userSignUpDto);
        UserEntity userEntity = createUser(userSignUpDto);
        return saveUser(userEntity);
    }

    public Page<UserEntity> search(String username, String firstName, String lastName, LocalDateTime createdFrom,
                                   LocalDateTime createdTo, String nationalCode, String organizationName,
                                   ContractType contractType, UserRole userRole, Long cif, int page, int size,
                                   String sortBy) {
        Pageable pageRequest = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Specification<UserEntity> userSpecification = Specification.where(UserSpecifications.hasUsername(username))
                .and(UserSpecifications.hasFirstName(firstName)).and(UserSpecifications.hasLastName(lastName))
                .and(UserSpecifications.createdFrom(createdFrom)).and(UserSpecifications.createdTo(createdTo))
                .and(UserSpecifications.hasNationalCode(nationalCode))
                .and(UserSpecifications.hasOrganizationName(organizationName))
                .and(UserSpecifications.hasContractType(contractType))
                .and(UserSpecifications.hasUserRole(userRole)).and(UserSpecifications.hasCif(cif));
        return userRepository.findAll(userSpecification, pageRequest);
    }

    private UserEntity saveUser(UserEntity userEntity) throws BadRequestException {
        try {
            return userRepository.save(userEntity);
        } catch (DataIntegrityViolationException exception) {
            throw new BadRequestException(exception.getMessage());
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

    private void validate(UserSignUpDto userSignUpDto) throws BadRequestException {
        if (userRepository.findUserEntityByNationalCode(userSignUpDto.getNationalCode()).isPresent())
            throw new BadRequestException(ErrorCode.DUPLICATION_NATIONAL_CODE_REGISTRATION);

        if (userRepository.findUserEntityByUsername(userSignUpDto.getUsername()).isPresent())
            throw new BadRequestException(ErrorCode.DUPLICATION_USERNAME_REGISTRATION);
    }

}