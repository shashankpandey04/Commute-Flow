package com.commuteflow.employee.repository;

import com.commuteflow.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Optional<Employee> findByOrganizationIdAndEmployeeCode(
        UUID organizationId,
        String employeeCode
    );

    boolean existsByOrganizationIdAndEmployeeCode(
        UUID organizationId,
        String employeeCode
    );
}