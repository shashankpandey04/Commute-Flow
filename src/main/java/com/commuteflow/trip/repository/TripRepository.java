package com.commuteflow.trip.repository;

import com.commuteflow.trip.entity.Trip;
import com.commuteflow.trip.entity.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TripRepository extends JpaRepository<Trip, UUID> {

    List<Trip> findByStatus(TripStatus status);

    List<Trip> findByRouteId(UUID routeId);

    List<Trip> findByDriverId(UUID driverId);

    List<Trip> findByVehicleId(UUID vehicleId);

    List<Trip> findByScheduledAtBetween(
        Instant start,
        Instant end
    );
}