package com.commuteflow.ride.entity;

import com.commuteflow.common.entity.BaseEntity;
import com.commuteflow.employee.entity.Employee;
import com.commuteflow.trip.entity.Trip;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "ride_events")
@Getter
@Setter
@NoArgsConstructor
public class RideEvent extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "trip_id",
        nullable = false
    )
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "employee_id",
        nullable = false
    )
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(
        nullable = false,
        columnDefinition = "ride_event_type"
    )
    private RideEventType eventType;

    @Column(
        name = "event_time",
        nullable = false
    )
    private Instant eventTime;
}