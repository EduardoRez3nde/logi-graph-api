package com.learning.logi.graph.api.domain.optimization.entities;

import ai.timefold.solver.core.api.domain.solution.*;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.learning.logi.graph.api.domain.route.repository.RouteRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@PlanningSolution
public class OptaRoutePlan {

    private String id;

    @PlanningEntityCollectionProperty
    private List<OptaVehicle> vehicles;

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "customerRange")
    private List<OptaCustomer> customers;

    @PlanningScore
    private HardSoftLongScore score;

    @ProblemFactProperty
    @JsonIgnore
    private RouteRepository routeRepository;

    public OptaRoutePlan(
            final String id,
            final List<OptaVehicle> vehicles,
            final List<OptaCustomer> customers,
            final RouteRepository routeRepository
    ) {
        this.id = id;
        this.vehicles = vehicles;
        this.customers = customers;
        this.routeRepository = routeRepository;
    }
}