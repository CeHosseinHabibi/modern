package com.habibi.modern.controller;

import com.habibi.modern.dto.UserSignUpDto;
import com.habibi.modern.dto.UserSignUpResponseDto;
import com.habibi.modern.entity.SignupRequest;
import com.habibi.modern.entity.UserEntity;
import com.habibi.modern.exceptions.SignUpException;
import com.habibi.modern.service.ConflictResolverService;
import com.habibi.modern.service.UserService;
import jakarta.validation.Valid;
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
    private ConflictResolverService conflictResolverService;

    @PostMapping()
    public ResponseEntity<UserSignUpResponseDto> signUp(@Valid @RequestBody UserSignUpDto userSignUpDto)
            throws SignUpException {
        SignupRequest signupRequest = conflictResolverService.saveSignUpRequest(userSignUpDto);
        UserEntity userEntity = userService.signUp(userSignUpDto, signupRequest);
        return ResponseEntity.ok(UserSignUpResponseDto.builder().username(userEntity.getUsername())
                .createdAt(userEntity.getCreatedAt()).build());
    }
}