package com.commuteflow.route.repository;

import com.commuteflow.route.entity.RouteStop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RouteStopRepository
        extends JpaRepository<RouteStop, UUID> {

    List<RouteStop> findByRouteIdOrderByStopOrder(UUID routeId);

    boolean existsByRouteIdAndStopOrder(
        UUID routeId,
        Integer stopOrder
    );
}