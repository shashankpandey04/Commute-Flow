package com.commuteflow.vehicle.service;

import com.commuteflow.common.exception.BadRequestException;
import com.commuteflow.common.exception.ResourceNotFoundException;
import com.commuteflow.organization.entity.Organization;
import com.commuteflow.organization.repository.OrganizationRepository;
import com.commuteflow.vehicle.dto.CreateVehicleRequest;
import com.commuteflow.vehicle.dto.VehicleResponse;
import com.commuteflow.vehicle.entity.Vehicle;
import com.commuteflow.vehicle.entity.VehicleStatus;
import com.commuteflow.vehicle.repository.VehicleRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final OrganizationRepository organizationRepository;

    @Transactional
    public VehicleResponse create(CreateVehicleRequest request) {

        String registrationNumber =
                request.registrationNumber().trim().toUpperCase();

        if (vehicleRepository
                .existsByRegistrationNumberIgnoreCase(registrationNumber)) {

            throw new BadRequestException(
                    "A vehicle with this registration number already exists"
            );
        }

        Organization organization =
                organizationRepository.findById(request.organizationId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Organization not found with id: "
                                                + request.organizationId()
                                )
                        );

        Vehicle vehicle = new Vehicle();

        vehicle.setOrganization(organization);
        vehicle.setRegistrationNumber(registrationNumber);
        vehicle.setModel(request.model().trim());
        vehicle.setCapacity(request.capacity());
        vehicle.setStatus(VehicleStatus.AVAILABLE);

        return VehicleResponse.from(
                vehicleRepository.save(vehicle)
        );
    }

    @Transactional(readOnly = true)
    public List<VehicleResponse> getAll() {

        return vehicleRepository.findAll()
                .stream()
                .map(VehicleResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public VehicleResponse getById(UUID id) {

        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vehicle not found with id: " + id
                        )
                );

        return VehicleResponse.from(vehicle);
    }

    @Transactional(readOnly = true)
    public List<VehicleResponse> getByOrganization(UUID organizationId) {

        if (!organizationRepository.existsById(organizationId)) {
            throw new ResourceNotFoundException(
                    "Organization not found with id: " + organizationId
            );
        }

        return vehicleRepository
                .findByOrganizationId(organizationId)
                .stream()
                .map(VehicleResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VehicleResponse> getAvailable(UUID organizationId) {

        if (!organizationRepository.existsById(organizationId)) {
            throw new ResourceNotFoundException(
                    "Organization not found with id: " + organizationId
            );
        }

        return vehicleRepository
                .findByOrganizationIdAndStatus(
                        organizationId,
                        VehicleStatus.AVAILABLE
                )
                .stream()
                .map(VehicleResponse::from)
                .toList();
    }
}