package com.commuteflow.ride.repository;

import com.commuteflow.ride.entity.RideEvent;
import com.commuteflow.ride.entity.RideEventType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RideEventRepository
        extends JpaRepository<RideEvent, UUID> {

    List<RideEvent> findByTripIdOrderByEventTimeAsc(
        UUID tripId
    );

    List<RideEvent> findByEmployeeIdOrderByEventTimeDesc(
        UUID employeeId
    );

    List<RideEvent> findByTripIdAndEventType(
        UUID tripId,
        RideEventType eventType
    );
}