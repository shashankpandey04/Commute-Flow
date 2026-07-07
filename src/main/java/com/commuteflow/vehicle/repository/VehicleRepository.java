package com.commuteflow.vehicle.repository;

import com.commuteflow.vehicle.entity.Vehicle;
import com.commuteflow.vehicle.entity.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {

    boolean existsByRegistrationNumber(String registrationNumber);

    List<Vehicle> findByStatus(VehicleStatus status);
}