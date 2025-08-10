package com.learning.logi.graph.api.domain.optimization.entities;

import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import com.learning.logi.graph.api.domain.optimization.enums.CustomerType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class OptaCustomer {

    @PlanningId
    private String id;

    private Long orderId;
    private CustomerType type;
    private Location location;
    private int demand;

    public OptaCustomer(
            final String id,
            final Long orderId,
            final Location location,
            final CustomerType type,
            final int demand
    ) {
        this.demand = demand;
        this.location = location;
        this.type = type;
        this.orderId = orderId;
        this.id = id;
    }
}