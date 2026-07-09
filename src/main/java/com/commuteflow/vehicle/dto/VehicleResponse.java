package com.commuteflow.vehicle.dto;

import com.commuteflow.vehicle.entity.Vehicle;
import com.commuteflow.vehicle.entity.VehicleStatus;

import java.time.Instant;
import java.util.UUID;

public record VehicleResponse(
        UUID id,
        UUID organizationId,
        String registrationNumber,
        String model,
        Integer capacity,
        VehicleStatus status,
        Instant createdAt,
        Instant updatedAt
) {

    public static VehicleResponse from(Vehicle vehicle) {
        return new VehicleResponse(
                vehicle.getId(),
                vehicle.getOrganization().getId(),
                vehicle.getRegistrationNumber(),
                vehicle.getModel(),
                vehicle.getCapacity(),
                vehicle.getStatus(),
                vehicle.getCreatedAt(),
                vehicle.getUpdatedAt()
        );
    }
}