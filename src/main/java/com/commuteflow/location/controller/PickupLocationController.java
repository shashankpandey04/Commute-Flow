package com.commuteflow.location.controller;

import com.commuteflow.common.response.ApiResponse;
import com.commuteflow.location.dto.CreatePickupLocationRequest;
import com.commuteflow.location.dto.PickupLocationResponse;
import com.commuteflow.location.service.PickupLocationService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pickup-locations")
@RequiredArgsConstructor
public class PickupLocationController {

    private final PickupLocationService pickupLocationService;

    @PostMapping
    public ResponseEntity<ApiResponse<PickupLocationResponse>> create(
            @Valid @RequestBody CreatePickupLocationRequest request
    ) {

        PickupLocationResponse response =
                pickupLocationService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Pickup location created successfully",
                                response
                        )
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PickupLocationResponse>>> getAll() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Pickup locations fetched successfully",
                        pickupLocationService.getAll()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PickupLocationResponse>> getById(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Pickup location fetched successfully",
                        pickupLocationService.getById(id)
                )
        );
    }
}