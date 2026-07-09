package com.commuteflow.vehicle.controller;

import com.commuteflow.common.response.ApiResponse;
import com.commuteflow.vehicle.dto.CreateVehicleRequest;
import com.commuteflow.vehicle.dto.VehicleResponse;
import com.commuteflow.vehicle.service.VehicleService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<ApiResponse<VehicleResponse>> create(
            @Valid @RequestBody CreateVehicleRequest request
    ) {

        VehicleResponse response =
                vehicleService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Vehicle created successfully",
                                response
                        )
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<VehicleResponse>>> getAll(
            @RequestParam(required = false) UUID organizationId,
            @RequestParam(required = false) Boolean available
    ) {

        List<VehicleResponse> vehicles;

        if (organizationId != null && Boolean.TRUE.equals(available)) {

            vehicles = vehicleService.getAvailable(organizationId);

        } else if (organizationId != null) {

            vehicles = vehicleService.getByOrganization(organizationId);

        } else {

            vehicles = vehicleService.getAll();
        }

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Vehicles fetched successfully",
                        vehicles
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VehicleResponse>> getById(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Vehicle fetched successfully",
                        vehicleService.getById(id)
                )
        );
    }
}