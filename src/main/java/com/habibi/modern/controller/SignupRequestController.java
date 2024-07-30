package com.habibi.modern.controller;

import com.habibi.modern.dto.PaginatedResponse;
import com.habibi.modern.dto.SignupRequestDto;
import com.habibi.modern.entity.SignupRequest;
import com.habibi.modern.service.SignupRequestService;
import com.habibi.modern.util.Utils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/signup-requests")
@AllArgsConstructor
public class SignupRequestController {
    private SignupRequestService signupRequestService;

    @GetMapping("/conflicts")
    public ResponseEntity<PaginatedResponse<SignupRequestDto>> getConflicts(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdTo,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Page<SignupRequest> conflictsPage = signupRequestService.getConflicts(createdFrom, createdTo, page, size, sortBy);
        PaginatedResponse<SignupRequestDto> response = Utils.toPaginatedDtos(conflictsPage, Utils::toSignupRequestDto);
        return ResponseEntity.ok(response);
    }
}