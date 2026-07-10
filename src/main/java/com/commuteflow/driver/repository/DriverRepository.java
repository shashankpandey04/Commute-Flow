package com.commuteflow.driver.repository;

import com.commuteflow.driver.entity.Driver;
import com.commuteflow.driver.entity.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DriverRepository extends JpaRepository<Driver, UUID> {

    boolean existsByLicenseNumberIgnoreCase(String licenseNumber);

    boolean existsByUserId(UUID userId);

    List<Driver> findByOrganizationId(UUID organizationId);

    List<Driver> findByOrganizationIdAndStatus(
            UUID organizationId,
            DriverStatus status
    );
}