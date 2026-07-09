package com.commuteflow.organization.dto;

import com.commuteflow.organization.entity.Organization;

import java.time.Instant;
import java.util.UUID;

public record OrganizationResponse(
        UUID id,
        String name,
        Instant createdAt,
        Instant updatedAt
) {

    public static OrganizationResponse from(Organization organization) {
        return new OrganizationResponse(
                organization.getId(),
                organization.getName(),
                organization.getCreatedAt(),
                organization.getUpdatedAt()
        );
    }
}