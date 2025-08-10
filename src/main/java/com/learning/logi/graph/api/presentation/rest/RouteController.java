package com.learning.logi.graph.api.presentation.rest;

import ai.timefold.solver.core.api.solver.SolverStatus;
import com.learning.logi.graph.api.domain.optimization.dto.RouteDTO;
import com.learning.logi.graph.api.domain.optimization.service.RouteOptimizationService;
import com.learning.logi.graph.api.domain.route.dto.JobResponse;
import com.learning.logi.graph.api.domain.route.dto.OptimizationRequest;
import com.learning.logi.graph.api.domain.route.dto.SimpleRouteDTO;
import com.learning.logi.graph.api.domain.route.dto.StatusResponse;
import com.learning.logi.graph.api.domain.route.service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.learning.logi.graph.api.domain.route.enums.SolverStatus.*;

@RestController
@RequestMapping("/routes")
public class RouteController {

    private final RouteService routeService;
    private final RouteOptimizationService optimizationService;

    public RouteController(
            final RouteService routeService,
            final RouteOptimizationService optimizationService
    ) {
        this.routeService = routeService;
        this.optimizationService = optimizationService;
    }

    @GetMapping("/simple")
    public ResponseEntity<SimpleRouteDTO> getSimpleRoute(
            @RequestParam final double startLat,
            @RequestParam final double startLon,
            @RequestParam final double endLat,
            @RequestParam final double endLon
    ) {
        final SimpleRouteDTO route = routeService.findSimpleRoute(startLat, startLon, endLat, endLon);
        return ResponseEntity.ok(route);
    }

    @PostMapping("/optimize")
    public ResponseEntity<JobResponse> optimizeRoutes(@RequestBody final OptimizationRequest request) {
        final UUID jobId = optimizationService.optimizeRoutes(request.orderIds());
        return ResponseEntity.accepted().body(new JobResponse(jobId.toString()));
    }

    @GetMapping("/optimize/status/{jobId}")
    public ResponseEntity<StatusResponse> getOptimizationStatus(@PathVariable final UUID jobId) {

        final SolverStatus status = optimizationService.getStatus(jobId);
        List<RouteDTO> solution = null;
        String userFriendlyStatus;

        if (status == null) {
            return ResponseEntity.notFound().build();
        }

        switch (status) {
            case SOLVING_SCHEDULED:
            case SOLVING_ACTIVE:
                userFriendlyStatus = "OTIMIZANDO";
                break;
            case NOT_SOLVING:
                solution = optimizationService.getSolution(jobId);
                userFriendlyStatus = (solution != null) ? "CONCLUIDO" : "FALHOU";
                break;
            default:
                userFriendlyStatus = "DESCONHECIDO";
                break;
        }

        return ResponseEntity.ok(new StatusResponse(jobId.toString(), userFriendlyStatus, solution));
    }
}
