package com.commuteflow.vehicle.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateVehicleRequest(

        @NotNull(message = "Organization ID is required")
        UUID organizationId,

        @NotBlank(message = "Registration number is required")
        @Size(max = 50, message = "Registration number must not exceed 50 characters")
        String registrationNumber,

        @NotBlank(message = "Vehicle model is required")
        @Size(max = 100, message = "Vehicle model must not exceed 100 characters")
        String model,

        @NotNull(message = "Vehicle capacity is required")
        @Min(value = 1, message = "Vehicle capacity must be at least 1")
        Integer capacity

) {}