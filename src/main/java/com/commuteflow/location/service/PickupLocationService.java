package com.commuteflow.location.service;

import com.commuteflow.common.exception.BadRequestException;
import com.commuteflow.common.exception.ResourceNotFoundException;
import com.commuteflow.location.dto.CreatePickupLocationRequest;
import com.commuteflow.location.dto.PickupLocationResponse;
import com.commuteflow.location.entity.PickupLocation;
import com.commuteflow.location.repository.PickupLocationRepository;
import com.commuteflow.organization.entity.Organization;
import com.commuteflow.organization.repository.OrganizationRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PickupLocationService {

    private final PickupLocationRepository pickupLocationRepository;
    private final OrganizationRepository organizationRepository;

    @Transactional
    public PickupLocationResponse create(
            CreatePickupLocationRequest request
    ) {

        Organization organization = organizationRepository
                .findById(request.organizationId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Organization not found with id: "
                                        + request.organizationId()
                        )
                );

        String name = request.name().trim();

        if (pickupLocationRepository
                .existsByOrganizationIdAndNameIgnoreCase(
                        request.organizationId(),
                        name
                )) {

            throw new BadRequestException(
                    "A pickup location with this name already exists"
            );
        }

        PickupLocation location = new PickupLocation();

        location.setOrganization(organization);
        location.setName(name);
        location.setAddress(request.address().trim());
        location.setLatitude(request.latitude());
        location.setLongitude(request.longitude());

        return PickupLocationResponse.from(
                pickupLocationRepository.save(location)
        );
    }

    @Transactional(readOnly = true)
    public List<PickupLocationResponse> getAll() {

        return pickupLocationRepository.findAll()
                .stream()
                .map(PickupLocationResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PickupLocationResponse getById(UUID id) {

        PickupLocation location = pickupLocationRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Pickup location not found with id: " + id
                        )
                );

        return PickupLocationResponse.from(location);
    }
}