package com.learning.logi.graph.api.domain.optimization.entities;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.variable.InverseRelationShadowVariable;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;
import com.learning.logi.graph.api.domain.optimization.enums.CustomerType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@PlanningEntity
public class OptaCustomer implements OptaStandstill {

    private Long orderId;
    private CustomerType type;
    private Location location;
    private int demand;

    @PlanningVariable(valueRangeProviderRefs = {"vehicleRange", "customerRange"})
    private OptaStandstill previousStandstill;

    @InverseRelationShadowVariable(sourceVariableName = "customers")
    private OptaVehicle vehicle;

    @Override
    public Location getLocation() {
        return location;
    }

    public OptaCustomer() { }
}
