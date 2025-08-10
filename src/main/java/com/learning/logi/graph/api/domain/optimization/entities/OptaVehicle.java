package com.learning.logi.graph.api.domain.optimization.entities;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.variable.PlanningListVariable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@PlanningEntity
public class OptaVehicle {

    @PlanningId
    private Long id;

    private Location depot;

    private int capacity;

    @PlanningListVariable(valueRangeProviderRefs = {"customerRange"})
    private final List<OptaCustomer> customers = new ArrayList<>();

    public OptaVehicle(
            final Long id,
            final Location depot,
            final int capacity
    ) {
        this.id = id;
        this.depot = depot;
        this.capacity = capacity;
    }
}