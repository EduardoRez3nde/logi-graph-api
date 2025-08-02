package com.learning.logi.graph.api.presentation.rest;

import com.learning.logi.graph.api.domain.route.dto.RouteDTO;
import com.learning.logi.graph.api.domain.route.service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping("/simples")
    public ResponseEntity<RouteDTO> getSimpleRoute(
            @RequestParam final double startLat,
            @RequestParam final double startLon,
            @RequestParam final double endLat,
            @RequestParam final double endLon
    ) {
        final RouteDTO route = routeService.findSimpleRoute(startLat, startLon, endLat, endLon);
        return ResponseEntity.ok(route);
    }
}
