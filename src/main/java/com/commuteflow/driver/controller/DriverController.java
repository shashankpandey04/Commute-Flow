package com.commuteflow.driver.controller;

import com.commuteflow.common.response.ApiResponse;
import com.commuteflow.driver.dto.CreateDriverRequest;
import com.commuteflow.driver.dto.DriverResponse;
import com.commuteflow.driver.service.DriverService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @PostMapping
    public ResponseEntity<ApiResponse<DriverResponse>> create(
            @Valid @RequestBody CreateDriverRequest request
    ) {

        DriverResponse response =
                driverService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Driver created successfully",
                                response
                        )
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DriverResponse>>> getAll(
            @RequestParam(required = false) UUID organizationId,
            @RequestParam(required = false) Boolean available
    ) {

        List<DriverResponse> drivers;

        if (organizationId != null && Boolean.TRUE.equals(available)) {

            drivers = driverService.getAvailable(organizationId);

        } else if (organizationId != null) {

            drivers = driverService.getByOrganization(organizationId);

        } else {

            drivers = driverService.getAll();
        }

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Drivers fetched successfully",
                        drivers
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DriverResponse>> getById(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Driver fetched successfully",
                        driverService.getById(id)
                )
        );
    }
}