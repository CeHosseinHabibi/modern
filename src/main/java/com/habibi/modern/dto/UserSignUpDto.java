package com.habibi.modern.dto;

import com.habibi.modern.enums.ContractType;
import com.habibi.modern.enums.UserRole;
import com.habibi.modern.enums.UserType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpDto {
    @Size(min = 3, max = 40, message = "The username size should be between 3 to 40.")
    @Pattern(regexp = "^(([a-z]+)|([A-Z]+))+([0-9]*)$", message = "The username must match the pattern.")
    private String username;

    @Size(min = 3, max = 40, message = "The password size should be between 3 to 40.")
    @Pattern(regexp = "^([0-9]+)([a-z]+)([A-Z]+)$", message = "The password must match the pattern.")
    private String password;

    @Size(min = 3, max = 60, message = "The first name size should be between 3 to 60.")
    private String firstName;

    @Size(min = 3, max = 60, message = "The last name size should be between 3 to 60.")
    private String lastName;

    @Min(value = 1, message = "The account number should be greater than 1.")
    @Max(value = 9999, message = "The account number should be less than 9999.")
    private Long accountNumber;

    @NotNull(message = "The user type should be not null.")
    private UserType userType;

    @NotBlank
    private String nationalCode;

    private String organizationName;
    private ContractType contractType;
    private UserRole userRole;
}