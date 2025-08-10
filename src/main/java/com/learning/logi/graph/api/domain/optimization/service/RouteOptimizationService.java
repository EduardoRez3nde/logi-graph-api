package com.learning.logi.graph.api.domain.optimization.service;

import ai.timefold.solver.core.api.solver.SolverManager;
import ai.timefold.solver.core.api.solver.SolverStatus;
import com.learning.logi.graph.api.domain.delivery_man.entities.DeliveryMan;
import com.learning.logi.graph.api.domain.delivery_man.repository.DeliveryManRepository;
import com.learning.logi.graph.api.domain.optimization.entities.Location;
import com.learning.logi.graph.api.domain.optimization.entities.OptaCustomer;
import com.learning.logi.graph.api.domain.optimization.entities.OptaRoutePlan;
import com.learning.logi.graph.api.domain.optimization.entities.OptaVehicle;
import com.learning.logi.graph.api.domain.optimization.enums.CustomerType;
import com.learning.logi.graph.api.domain.order.entities.Order;
import com.learning.logi.graph.api.domain.order.repository.OrderRepository;
import com.learning.logi.graph.api.domain.route.dto.CoordinatesDTO;
import com.learning.logi.graph.api.domain.route.dto.DijkstraResult;
import com.learning.logi.graph.api.domain.optimization.dto.RouteDTO;
import com.learning.logi.graph.api.domain.route.repository.RouteRepository;
import com.learning.logi.graph.api.presentation.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RouteOptimizationService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RouteOptimizationService.class);

    private final SolverManager<OptaRoutePlan, UUID> solverManager;
    private final RedisTemplate<String, OptaRoutePlan> redisTemplate;
    private final DeliveryManRepository deliveryManRepository;
    private final OrderRepository orderRepository;
    private final RouteRepository routeRepository;

    public RouteOptimizationService(
            final SolverManager<OptaRoutePlan, UUID> solverManager,
            final RedisTemplate<String, OptaRoutePlan> redisTemplate,
            final DeliveryManRepository deliveryManRepository,
            final OrderRepository orderRepository,
            final RouteRepository routeRepository
    ) {
        this.solverManager = solverManager;
        this.redisTemplate = redisTemplate;
        this.deliveryManRepository = deliveryManRepository;
        this.orderRepository = orderRepository;
        this.routeRepository = routeRepository;
    }

    public UUID optimizeRoutes(final List<Long> orderIds) {

        final UUID problemId = UUID.randomUUID();
        final OptaRoutePlan problem = buildProblem(orderIds, problemId);

        solverManager.solveBuilder()
                .withProblemId(problemId)
                .withProblemFinder(id -> problem)
                .withFinalBestSolutionConsumer(solution -> {
                    final String solutionKey = "job:solution:" + problemId;
                    redisTemplate.opsForValue().set(solutionKey, solution, Duration.ofHours(24));
                    LOGGER.info("Solução para o trabalho {} salva do Redis.", problemId);
                })
                .withExceptionHandler((id, throwables) -> {
                    LOGGER.error("Ocorreu um erro ao otimizar a rota: {}", id, throwables);
                    throw new RuntimeException("Falha na otimização para o trabalho " + id, throwables);
                    // TODO: implementar um erro customizado
                })
                .run();
        return problemId;
    }

    private OptaRoutePlan buildProblem(final List<Long> orderIds, final UUID problemId) {

        LOGGER.info("Construindo o problema de otimização para {} pedidos...", orderIds.size());

        final List<Order> orderToDelivery = orderRepository.findAllById(orderIds);
        if (orderToDelivery.isEmpty())
            throw new ResourceNotFoundException("Nenhum Pedido para otimização.");

        final double centerLat = orderToDelivery.stream()
                .mapToDouble(order -> order.getCollectionPoint().getY())
                .average().orElse(0.0);

        final double centerLon = orderToDelivery.stream()
                .mapToDouble(order -> order.getCollectionPoint().getX())
                .average().orElse(0.0);

        final double searchRadiusMeters = 5000.0;
        LOGGER.info("Procurando entregadores AVAILABLE perto de (lat: {}, lon: {}) dentro de um raio de {}m", centerLat, centerLon, searchRadiusMeters);

        final List<DeliveryMan> availableDeliveryMen = deliveryManRepository.findAvailableDriversNearPoint(centerLat, centerLon, searchRadiusMeters);

        if (availableDeliveryMen.isEmpty())
            throw new ResourceNotFoundException("Nenhum entregador disponível encontrado na região dos pedidos");

        LOGGER.info("{} entregadores encontrados na área.", availableDeliveryMen.size());

        final List<OptaVehicle> optaVehicles = availableDeliveryMen.stream()
                .map(deliveryMan -> {
                    final Long startNodeId = routeRepository.findNearestNodeId(
                            deliveryMan.getCurrentLocation().getY(),
                            deliveryMan.getCurrentLocation().getX()
                    );
                    final Location location = Location.create(
                            startNodeId,
                            deliveryMan.getCurrentLocation().getY(),
                            deliveryMan.getCurrentLocation().getX()
                    );
                    final int capacity = deliveryMan.getVehicle().getMaxCapacityKg().intValue();

                    return new OptaVehicle(deliveryMan.getId(), location, capacity);
                }).toList();

        final List<OptaCustomer> optaCustomers = new ArrayList<>();

        for (Order order : orderToDelivery) {
            final Long pickupNodeId = routeRepository.findNearestNodeId(
                    order.getCollectionPoint().getY(),
                    order.getCollectionPoint().getX()
            );

            final Location pickupLocation = Location.create(
                    pickupNodeId,
                    order.getCollectionPoint().getY(),
                    order.getCollectionPoint().getX()
            );

            String pickupId = order.getId() + "-COLLECT";

            optaCustomers.add(new OptaCustomer(
                    pickupId,
                    order.getId(),
                    pickupLocation,
                    CustomerType.COLLECTION,
                    1
            ));
            final Long deliveryNodeId = routeRepository.findNearestNodeId(
                    order.getDeliveredPoint().getY(),
                    order.getDeliveredPoint().getX()
            );

            final Location deliveryLocation = Location.create(
                    deliveryNodeId,
                    order.getDeliveredPoint().getY(),
                    order.getDeliveredPoint().getX()
            );

            String deliveryId = order.getId() + "-DELIVERY";

            optaCustomers.add(new OptaCustomer(
                    deliveryId,
                    order.getId(),
                    deliveryLocation,
                    CustomerType.DELIVERY,
                    -1
            ));
        }

        final OptaRoutePlan problem = new OptaRoutePlan(problemId.toString(), optaVehicles, optaCustomers, this.routeRepository);
        LOGGER.info("Problema constuído com {} veículos e {} paradas", optaVehicles.size(), optaCustomers.size());
        return problem;
    }

    public SolverStatus getStatus(final UUID problemId) {
        return solverManager.getSolverStatus(problemId);
    }

    public List<RouteDTO> getSolution(final UUID problemId) {

        final String solutionKey = "job:solution:" + problemId;
        final OptaRoutePlan solution = (OptaRoutePlan) redisTemplate.opsForValue().get(solutionKey);

        if (solution == null) return null;

        return formatSolution(solution);
    }

    private List<RouteDTO> formatSolution(final OptaRoutePlan solution) {

        LOGGER.info("Formatando a solução encontrada com pontuação: {}", solution.getScore());
        final List<RouteDTO> routes = new ArrayList<>();

        for (final OptaVehicle vehicle : solution.getVehicles()) {
            final List<OptaCustomer> customerRoute = vehicle.getCustomers();

            if (customerRoute.isEmpty()) {
                continue;
            }

            final List<CoordinatesDTO> fullPathCoordinates = new ArrayList<>();
            double totalDistanceForRoute = 0.0;

            Location previousLocation = vehicle.getDepot();
            fullPathCoordinates.add(new CoordinatesDTO(vehicle.getDepot().longitude(), vehicle.getDepot().latitude()));

            for (final OptaCustomer currentStop : customerRoute) {
                final DijkstraResult segmentResult = routeRepository.findShortestPathDijkstra(
                        previousLocation.nodeId(),
                        currentStop.getLocation().nodeId()
                );

                if (segmentResult != null && segmentResult.nodeIds() != null && !segmentResult.nodeIds().isEmpty()) {
                    totalDistanceForRoute += segmentResult.totalCost();

                    final List<CoordinatesDTO> segmentCoordinates = routeRepository.getCoordinatesForNodeIds(segmentResult.nodeIds());

                    if (!segmentCoordinates.isEmpty()) {
                        segmentCoordinates.remove(0);
                    }

                    fullPathCoordinates.addAll(segmentCoordinates);
                }
                previousLocation = currentStop.getLocation();
            }
            routes.add(new RouteDTO(vehicle.getId(), fullPathCoordinates, totalDistanceForRoute));
        }
        LOGGER.info("Solução formatada em {} rotas.", routes.size());
        return routes;
    }
}
