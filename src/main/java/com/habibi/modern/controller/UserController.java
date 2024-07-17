package com.habibi.modern.controller;

import com.habibi.modern.dto.UserSignUpDto;
import com.habibi.modern.dto.UserSignUpResponseDto;
import com.habibi.modern.entity.SignupRequest;
import com.habibi.modern.entity.UserEntity;
import com.habibi.modern.enums.ContractType;
import com.habibi.modern.enums.UserRole;
import com.habibi.modern.exceptions.SignUpException;
import com.habibi.modern.service.ConflictResolverService;
import com.habibi.modern.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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

    @GetMapping()
    public Page<UserEntity> search(
            @RequestParam(required = false) String username, @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdTo,
            @RequestParam(required = false) String nationalCode, @RequestParam(required = false) String organizationName,
            @RequestParam(required = false) ContractType contractType, @RequestParam(required = false) UserRole userRole,
            @RequestParam(required = false) Long cif, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy) {
        return userService.search(username, firstName, lastName, createdFrom, createdTo, nationalCode,
                organizationName, contractType, userRole, cif, page, size, sortBy);
    }
}