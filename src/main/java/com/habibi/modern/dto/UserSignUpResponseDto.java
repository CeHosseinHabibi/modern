package com.habibi.modern.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class UserSignUpResponseDto {
    private String username;
    private LocalDateTime createdAt;
}