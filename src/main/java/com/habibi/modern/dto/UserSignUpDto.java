package com.habibi.modern.dto;

import com.habibi.modern.enums.ContractType;
import com.habibi.modern.enums.UserRole;
import com.habibi.modern.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpDto {
    private String username;
    private String password;
    private Long accountNumber;
    private UserType userType;
    private String organizationName;
    private ContractType contractType;
    private UserRole userRole;
}