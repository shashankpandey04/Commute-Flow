package com.commuteflow.location.dto;

import com.commuteflow.location.entity.PickupLocation;

import java.time.Instant;
import java.util.UUID;

public record PickupLocationResponse(
        UUID id,
        UUID organizationId,
        String name,
        String address,
        Double latitude,
        Double longitude,
        Instant createdAt,
        Instant updatedAt
) {

    public static PickupLocationResponse from(PickupLocation location) {
        return new PickupLocationResponse(
                location.getId(),
                location.getOrganization().getId(),
                location.getName(),
                location.getAddress(),
                location.getLatitude(),
                location.getLongitude(),
                location.getCreatedAt(),
                location.getUpdatedAt()
        );
    }
}