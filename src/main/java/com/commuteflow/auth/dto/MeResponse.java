package com.commuteflow.auth.dto;

import com.commuteflow.user.entity.User;

import java.util.UUID;

public record MeResponse(
        UUID id,
        String email,
        String role
) {
    public static MeResponse from(User user) {
        return new MeResponse(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}