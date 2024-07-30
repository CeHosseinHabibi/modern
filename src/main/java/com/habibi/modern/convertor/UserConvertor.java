package com.habibi.modern.convertor;

import com.habibi.modern.dto.BankUserDto;
import com.habibi.modern.dto.ThirdPartyUserDto;
import com.habibi.modern.dto.UserDto;
import com.habibi.modern.entity.BankUser;
import com.habibi.modern.entity.ThirdPartyUser;
import com.habibi.modern.entity.UserEntity;

public class UserConvertor {
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