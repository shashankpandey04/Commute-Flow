package com.commuteflow.route.repository;

import com.commuteflow.route.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RouteRepository extends JpaRepository<Route, UUID> {

    List<Route> findByOrganizationId(UUID organizationId);
}