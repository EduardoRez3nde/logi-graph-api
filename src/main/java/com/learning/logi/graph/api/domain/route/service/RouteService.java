package com.learning.logi.graph.api.domain.route.service;

import com.learning.logi.graph.api.domain.route.dto.CoordinatesDTO;
import com.learning.logi.graph.api.domain.route.dto.DijkstraResult;
import com.learning.logi.graph.api.domain.route.dto.RouteDTO;
import com.learning.logi.graph.api.domain.route.repository.RouteRepository;
import com.learning.logi.graph.api.presentation.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class RouteService {

    private final RouteRepository routeRepository;


    public RouteService(final RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    @Transactional(readOnly = true)
    public RouteDTO findSimpleRoute(final double startLat, final double startLon, final double endLat, final double endLon) {

        final Long startNodeId = routeRepository.findNearestNodeId(startLat, startLon);
        final Long endNodeId = routeRepository.findNearestNodeId(endLat, endLon);

        if (startNodeId == null || endNodeId == null) {
            throw new ResourceNotFoundException("Unable to find entrance bridges/said no map for given coordinates.");
        }

        final DijkstraResult pathResult = routeRepository.findShortestPathDijkstra(startNodeId, endNodeId);

        if (pathResult == null) {
            throw new RuntimeException("It is not possible to calculate a rotation between bridges.");
        }

        final List<Long> nodeIds = pathResult.nodeIds();
        final Double totalCost = pathResult.totalCost();

        final List<Map<String, Object>> coordinates = routeRepository.getCoordinatesForNodeIds(nodeIds);

        final List<CoordinatesDTO> path = coordinates.stream()
                .map(coordinate -> new CoordinatesDTO(
                        (Double) coordinate.get("longitude"),
                        (Double) coordinate.get("latitude")
                )).toList();

        return new RouteDTO(path, totalCost);
    }
}
