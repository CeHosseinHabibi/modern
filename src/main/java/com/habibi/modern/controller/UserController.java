package com.habibi.modern.controller;

import com.habibi.modern.dto.UserSignUpDto;
import com.habibi.modern.dto.UserSignUpResponseDto;
import com.habibi.modern.entity.UserEntity;
import com.habibi.modern.exceptions.SignUpException;
import com.habibi.modern.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping()
    public ResponseEntity<UserSignUpResponseDto> signUp(@RequestBody UserSignUpDto userSignUpDto) throws SignUpException {
        UserEntity userEntity = userService.signUp(userSignUpDto);
        return ResponseEntity.ok(UserSignUpResponseDto.builder().username(userEntity.getUsername())
                .createdAt(userEntity.getCreatedAt()).build());
    }
}