package com.learning.logi.graph.api.domain.optimization.entities;

import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.solution.ProblemFactCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.ProblemFactProperty;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import com.learning.logi.graph.api.domain.route.repository.RouteRepository;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@PlanningSolution
public class OptaRoutePlan {

    @ProblemFactCollectionProperty
    private List<OptaVehicle> vehicles;

    @ValueRangeProvider
    @ProblemFactCollectionProperty
    private List<OptaCustomer> customers;

    @PlanningScore
    @ValueRangeProvider
    @ProblemFactCollectionProperty
    private HardSoftLongScore score;

    @ProblemFactProperty
    private RouteRepository routeRepository;

    public OptaRoutePlan() { }

    public OptaRoutePlan(final List<OptaVehicle> vehicles, final List<OptaCustomer> customers, final RouteRepository routeRepository) {
        this.vehicles = vehicles;
        this.customers = customers;
        this.routeRepository = routeRepository;
    }
}
