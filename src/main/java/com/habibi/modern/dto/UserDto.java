package com.habibi.modern.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class UserDto {
    private String type;
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private String nationalCode;
}