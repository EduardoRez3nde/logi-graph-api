package com.learning.logi.graph.api.domain.optimization.entities;

import ai.timefold.solver.core.api.score.stream.*;
import com.learning.logi.graph.api.domain.route.dto.DijkstraResult;
import com.learning.logi.graph.api.domain.route.repository.RouteRepository;

import java.util.function.Function;

import static ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore.ONE_HARD;
import static ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore.ONE_SOFT;


public class RouteConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(final ConstraintFactory constraintFactory) {
        return new Constraint[] {
                vehicleCapacity(constraintFactory),
        };
    }

    private Constraint vehicleCapacity(final ConstraintFactory constraintFactory) {

        return constraintFactory.forEach(OptaVehicle.class)
                .join(OptaCustomer.class,
                        Joiners.equal(Function.identity(), OptaCustomer::getVehicle))
                .groupBy((vehicle, customer) -> vehicle,
                        ConstraintCollectors.sum((vehicle, customer) -> customer.getDemand()))
                .filter((vehicle, totalDemand) -> totalDemand > vehicle.getCapacity())
                .penalizeLong(ONE_HARD, (vehicle, totalDemand) -> totalDemand - vehicle.getCapacity())
                .asConstraint("Vehicle Capacity");
    }

    private Constraint minimizeTotalDistance(final ConstraintFactory constraintFactory) {

        return constraintFactory.forEach(OptaCustomer.class)
                .filter(customer -> customer.getPreviousStandstill() != null)
                .join(RouteRepository.class)
                .penalizeLong(ONE_SOFT,
                        (customer, repository) -> getDistance(repository, customer.getPreviousStandstill(), customer)
                )
                .asConstraint("Minimize Total Distance");
    }

    private Long getDistance(final RouteRepository routeRepository, final OptaStandstill from, OptaCustomer to) {

        final Location fromLocation = from.getLocation();
        final Location toLocation = to.getLocation();

        final DijkstraResult result = routeRepository.findShortestPathDijkstra(fromLocation.nodeId(), toLocation.nodeId());

        if (result == null || result.totalCost() == null)
            return Long.MAX_VALUE;

        return result.totalCost().longValue();
    }
}
