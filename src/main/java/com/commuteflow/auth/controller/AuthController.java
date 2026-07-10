package com.commuteflow.auth.controller;

import com.commuteflow.auth.dto.AuthResponse;
import com.commuteflow.auth.dto.LoginRequest;
import com.commuteflow.auth.dto.MeResponse;
import com.commuteflow.auth.dto.RegisterRequest;
import com.commuteflow.auth.dto.UpdateRoleRequest;
import com.commuteflow.auth.service.AuthService;
import com.commuteflow.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Account created successfully",
                                null
                        )
                );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {

        AuthResponse response =
                authService.login(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Login successful",
                        response
                )
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MeResponse>> me(
            Authentication authentication
    ) {

        UUID userId = (UUID) authentication.getPrincipal();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Current user fetched successfully",
                        authService.getCurrentUser(userId)
                )
        );
    }

    @PatchMapping("/users/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MeResponse>> updateRole(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateRoleRequest request
    ) {

        MeResponse response =
                authService.updateRole(userId, request.role());

        return ResponseEntity.ok(
                ApiResponse.success(
                        "User role updated successfully",
                        response
                )
        );
    }

}