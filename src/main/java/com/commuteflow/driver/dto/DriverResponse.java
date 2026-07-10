package com.commuteflow.driver.dto;

import com.commuteflow.driver.entity.Driver;
import com.commuteflow.driver.entity.DriverStatus;

import java.time.Instant;
import java.util.UUID;

public record DriverResponse(
        UUID id,
        UUID userId,
        UUID organizationId,
        String licenseNumber,
        DriverStatus status,
        Instant createdAt,
        Instant updatedAt
) {

    public static DriverResponse from(Driver driver) {
        return new DriverResponse(
                driver.getId(),
                driver.getUser().getId(),
                driver.getOrganization().getId(),
                driver.getLicenseNumber(),
                driver.getStatus(),
                driver.getCreatedAt(),
                driver.getUpdatedAt()
        );
    }
}