package com.commuteflow.route.entity;

import com.commuteflow.common.entity.BaseEntity;
import com.commuteflow.location.entity.PickupLocation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(
    name = "route_stops",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_route_stop_order",
            columnNames = {"route_id", "stop_order"}
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
public class RouteStop extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "route_id",
        nullable = false
    )
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "pickup_location_id",
        nullable = false
    )
    private PickupLocation pickupLocation;

    @Column(name = "stop_order", nullable = false)
    private Integer stopOrder;

    @Column(name = "estimated_arrival")
    private LocalTime estimatedArrival;
}