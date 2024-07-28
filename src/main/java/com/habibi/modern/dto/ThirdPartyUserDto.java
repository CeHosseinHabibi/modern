package com.habibi.modern.dto;

import com.habibi.modern.enums.ContractType;
import com.habibi.modern.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThirdPartyUserDto extends UserDto {
    private String organizationName;
    private ContractType contractType;
    private UserRole userRole;
}