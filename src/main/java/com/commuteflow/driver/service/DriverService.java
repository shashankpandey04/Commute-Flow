package com.commuteflow.driver.service;

import com.commuteflow.common.exception.BadRequestException;
import com.commuteflow.common.exception.ResourceNotFoundException;
import com.commuteflow.driver.dto.CreateDriverRequest;
import com.commuteflow.driver.dto.DriverResponse;
import com.commuteflow.driver.entity.Driver;
import com.commuteflow.driver.entity.DriverStatus;
import com.commuteflow.driver.repository.DriverRepository;
import com.commuteflow.organization.entity.Organization;
import com.commuteflow.organization.repository.OrganizationRepository;
import com.commuteflow.user.entity.User;
import com.commuteflow.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;

    @Transactional
    public DriverResponse create(CreateDriverRequest request) {

        String licenseNumber =
                request.licenseNumber().trim().toUpperCase();

        if (driverRepository.existsByLicenseNumberIgnoreCase(licenseNumber)) {
            throw new BadRequestException(
                    "A driver with this license number already exists"
            );
        }

        if (driverRepository.existsByUserId(request.userId())) {
            throw new BadRequestException(
                    "This user is already registered as a driver"
            );
        }

        User user = userRepository.findById(request.userId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + request.userId()
                        )
                );

        Organization organization =
                organizationRepository.findById(request.organizationId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Organization not found with id: "
                                                + request.organizationId()
                                )
                        );

        Driver driver = new Driver();

        driver.setUser(user);
        driver.setOrganization(organization);
        driver.setLicenseNumber(licenseNumber);
        driver.setStatus(DriverStatus.AVAILABLE);

        return DriverResponse.from(
                driverRepository.save(driver)
        );
    }

    @Transactional(readOnly = true)
    public List<DriverResponse> getAll() {

        return driverRepository.findAll()
                .stream()
                .map(DriverResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public DriverResponse getById(UUID id) {

        Driver driver = driverRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Driver not found with id: " + id
                        )
                );

        return DriverResponse.from(driver);
    }

    @Transactional(readOnly = true)
    public List<DriverResponse> getByOrganization(UUID organizationId) {

        if (!organizationRepository.existsById(organizationId)) {
            throw new ResourceNotFoundException(
                    "Organization not found with id: " + organizationId
            );
        }

        return driverRepository.findByOrganizationId(organizationId)
                .stream()
                .map(DriverResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DriverResponse> getAvailable(UUID organizationId) {

        if (!organizationRepository.existsById(organizationId)) {
            throw new ResourceNotFoundException(
                    "Organization not found with id: " + organizationId
            );
        }

        return driverRepository
                .findByOrganizationIdAndStatus(
                        organizationId,
                        DriverStatus.AVAILABLE
                )
                .stream()
                .map(DriverResponse::from)
                .toList();
    }
}