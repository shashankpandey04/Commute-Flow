package com.commuteflow.organization.controller;

import com.commuteflow.common.response.ApiResponse;
import com.commuteflow.organization.dto.CreateOrganizationRequest;
import com.commuteflow.organization.dto.OrganizationResponse;
import com.commuteflow.organization.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrganizationResponse>> create(
            @Valid @RequestBody CreateOrganizationRequest request
    ) {
        OrganizationResponse response = organizationService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Organization created successfully",
                        response
                ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrganizationResponse>>> getAll() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Organizations fetched successfully",
                        organizationService.getAll()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrganizationResponse>> getById(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Organization fetched successfully",
                        organizationService.getById(id)
                )
        );
    }
}