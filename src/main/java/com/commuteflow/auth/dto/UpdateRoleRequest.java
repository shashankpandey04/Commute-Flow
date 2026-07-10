package com.commuteflow.auth.dto;

import com.commuteflow.user.entity.UserRole;
import jakarta.validation.constraints.NotNull;

public record UpdateRoleRequest(

        @NotNull(message = "Role is required")
        UserRole role

) {}