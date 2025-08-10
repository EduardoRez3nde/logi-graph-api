package com.learning.logi.graph.api.domain.optimization.entities;

import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import com.learning.logi.graph.api.domain.route.dto.DijkstraResult;
import com.learning.logi.graph.api.domain.route.repository.RouteRepository;

import static ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore.ONE_HARD;
import static ai.timefold.solver.core.api.score.buildin.hardsoftlong.HardSoftLongScore.ONE_SOFT;

public class RouteConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(final ConstraintFactory factory) {
        return new Constraint[]{
                vehicleCapacity(factory),
                minimizeTotalDistance(factory)
        };
    }

    private Constraint vehicleCapacity(final ConstraintFactory factory) {
        return factory.forEach(OptaVehicle.class)
                .filter(vehicle -> vehicle.getCustomers().stream()
                        .mapToInt(OptaCustomer::getDemand)
                        .sum() > vehicle.getCapacity()
                )
                .penalizeLong(ONE_HARD,
                        vehicle -> (long) (vehicle.getCustomers().stream()
                                .mapToInt(OptaCustomer::getDemand)
                                .sum() - vehicle.getCapacity())
                )
                .asConstraint("Vehicle Capacity");
    }

    private Constraint minimizeTotalDistance(final ConstraintFactory factory) {
        return factory.forEach(OptaVehicle.class)
                .join(RouteRepository.class)
                .penalizeLong(ONE_SOFT,
                        (vehicle, repository) -> calculateRouteDistance(repository, vehicle)
                )
                .asConstraint("Minimize Total Distance");
    }

    private long calculateRouteDistance(final RouteRepository routeRepository, final OptaVehicle vehicle) {

        if (vehicle.getCustomers().isEmpty()) {
            return 0L;
        }

        long totalDistance = 0L;
        Location previousLocation = vehicle.getDepot();

        for (final OptaCustomer customer : vehicle.getCustomers()) {
            totalDistance += getDistanceBetweenLocations(routeRepository, previousLocation, customer.getLocation());
            previousLocation = customer.getLocation();
        }
        return totalDistance;
    }

    private long getDistanceBetweenLocations(final RouteRepository routeRepository, final Location from, final Location to) {
        final DijkstraResult result = routeRepository.findShortestPathDijkstra(from.nodeId(), to.nodeId());

        if (result == null || result.totalCost() == null) {
            return Long.MAX_VALUE / 1000;
        }
        return result.totalCost().longValue();
    }
}