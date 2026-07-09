package com.commuteflow.location.repository;

import com.commuteflow.location.entity.PickupLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PickupLocationRepository extends JpaRepository<PickupLocation, UUID> {

    boolean existsByOrganizationIdAndNameIgnoreCase(
            UUID organizationId,
            String name
    );
}