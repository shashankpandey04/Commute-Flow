package com.commuteflow.organization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOrganizationRequest(

        @NotBlank(message = "Organization name is required")
        @Size(max = 150, message = "Organization name must not exceed 150 characters")
        String name

) {}