package com.commuteflow.driver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateDriverRequest(

        @NotNull(message = "User ID is required")
        UUID userId,

        @NotNull(message = "Organization ID is required")
        UUID organizationId,

        @NotBlank(message = "License number is required")
        @Size(max = 50, message = "License number must not exceed 50 characters")
        String licenseNumber

) {}