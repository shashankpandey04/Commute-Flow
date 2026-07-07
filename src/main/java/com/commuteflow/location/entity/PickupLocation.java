package com.commuteflow.location.entity;

import com.commuteflow.common.entity.BaseEntity;
import com.commuteflow.organization.entity.Organization;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pickup_locations")
@Getter
@Setter
@NoArgsConstructor
public class PickupLocation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "organization_id",
        nullable = false
    )
    private Organization organization;

    @Column(nullable = false)
    private String name;

    @Column(
        nullable = false,
        columnDefinition = "text"
    )
    private String address;

    private Double latitude;

    private Double longitude;
}