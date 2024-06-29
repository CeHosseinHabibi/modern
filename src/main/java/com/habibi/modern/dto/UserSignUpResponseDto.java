package com.habibi.modern.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class UserSignUpResponseDto {
    private String username;
    private Date createdAt;
}