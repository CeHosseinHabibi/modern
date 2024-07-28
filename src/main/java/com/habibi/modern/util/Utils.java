package com.habibi.modern.util;

import com.habibi.modern.dto.BankUserDto;
import com.habibi.modern.dto.RequesterDto;
import com.habibi.modern.dto.ThirdPartyUserDto;
import com.habibi.modern.dto.UserDto;
import com.habibi.modern.entity.BankUser;
import com.habibi.modern.entity.RequesterEntity;
import com.habibi.modern.entity.ThirdPartyUser;
import com.habibi.modern.entity.UserEntity;

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
}