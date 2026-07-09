package com.commuteflow.organization.service;

import com.commuteflow.common.exception.BadRequestException;
import com.commuteflow.common.exception.ResourceNotFoundException;
import com.commuteflow.organization.dto.CreateOrganizationRequest;
import com.commuteflow.organization.dto.OrganizationResponse;
import com.commuteflow.organization.entity.Organization;
import com.commuteflow.organization.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Transactional
    public OrganizationResponse create(CreateOrganizationRequest request) {

        String name = request.name().trim();

        if (organizationRepository.existsByNameIgnoreCase(name)) {
            throw new BadRequestException(
                    "An organization with this name already exists"
            );
        }

        Organization organization = new Organization();
        organization.setName(name);

        return OrganizationResponse.from(
                organizationRepository.save(organization)
        );
    }

    @Transactional(readOnly = true)
    public List<OrganizationResponse> getAll() {
        return organizationRepository.findAll()
                .stream()
                .map(OrganizationResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrganizationResponse getById(UUID id) {

        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Organization not found with id: " + id
                        )
                );

        return OrganizationResponse.from(organization);
    }
}