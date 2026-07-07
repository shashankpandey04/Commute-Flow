package com.commuteflow.booking.repository;

import com.commuteflow.booking.entity.Booking;
import com.commuteflow.booking.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    boolean existsByTripIdAndEmployeeId(
        UUID tripId,
        UUID employeeId
    );

    List<Booking> findByTripId(UUID tripId);

    List<Booking> findByEmployeeId(UUID employeeId);

    List<Booking> findByTripIdAndStatus(
        UUID tripId,
        BookingStatus status
    );

    long countByTripIdAndStatus(
        UUID tripId,
        BookingStatus status
    );
}