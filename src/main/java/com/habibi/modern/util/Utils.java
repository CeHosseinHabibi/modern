package com.habibi.modern.util;

import com.habibi.modern.dto.*;
import com.habibi.modern.entity.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Utils {
    public static final String CORE_SERVICE_URL = "http://localhost:8081";
    public static final String ACCOUNTS = "accounts";
    public static final String SLASH = "/";
    public static final String WITHDRAW = "withdraw";
    public static final String ROLLBACK_WITHDRAW = "rollback-withdraw";

    public static RequesterEntity getRequesterEntity(RequesterDto requesterDto) {
        return new RequesterEntity(requesterDto.getRequestedAt(), requesterDto.getUserNationalCode());
    }

    public static RequesterDto getRequesterDto(RequesterEntity requesterEntity) {
        return requesterEntity == null ?
                null : new RequesterDto(requesterEntity.getRequestedAt(), requesterEntity.getUserNationalCode());
    }

    public static UserDto toUserDto(UserEntity userEntity) {
        if (userEntity == null)
            return null;
        UserDto userDto = null;
        if (userEntity instanceof BankUser) {
            BankUser bankUser = (BankUser) userEntity;
            userDto = new BankUserDto(bankUser.getCif());
            userDto.setType("BankUser");
        } else if (userEntity instanceof ThirdPartyUser) {
            ThirdPartyUser thirdPartyUser = (ThirdPartyUser) userEntity;
            userDto = new ThirdPartyUserDto(thirdPartyUser.getOrganizationName(), thirdPartyUser.getContractType()
                    , thirdPartyUser.getUserRole());
            userDto.setType("ThirdPartyUser");
        }
        userDto.setId(userEntity.getId());
        userDto.setUsername(userEntity.getUsername());
        userDto.setFirstName(userEntity.getFirstName());
        userDto.setLastName(userEntity.getLastName());
        userDto.setCreatedAt(userEntity.getCreatedAt());
        userDto.setNationalCode(userEntity.getNationalCode());
        return userDto;
    }

    public static SignupRequestDto toSignupRequestDto(SignupRequest signupRequest) {
        return signupRequest == null ? null : new SignupRequestDto(signupRequest.getId(),
                signupRequest.getRequestStatus(), signupRequest.getLastRollbackTryErrorCode(),
                signupRequest.getLastRollbackTryDescription(), signupRequest.getLastRollbackTryDate(),
                getRequesterDto(signupRequest.getRequesterEntity()));
    }

    public static <ReturnDtoType, SourceType> PaginatedResponse toPaginatedDtos(
            Page<SourceType> entitiesPage, Function<SourceType, ReturnDtoType> entityToDtoMapperFunction) {
        List dtos = entitiesPage.stream().map(entityToDtoMapperFunction).collect(Collectors.toList());
        PaginatedResponse response = new PaginatedResponse<>(dtos, entitiesPage.getNumber(), entitiesPage.getSize(),
                entitiesPage.hasNext(), entitiesPage.hasPrevious(), entitiesPage.getTotalElements(),
                entitiesPage.getTotalPages());
        return response;
    }
}