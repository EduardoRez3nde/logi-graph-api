package com.learning.logi.graph.api.presentation.rest;

import com.learning.logi.graph.api.domain.delivery_man.entities.DeliveryMan;
import com.learning.logi.graph.api.domain.driver.dto.LocationUpdateRequest;
import com.learning.logi.graph.api.domain.driver.dto.TrackingResponse;
import com.learning.logi.graph.api.domain.driver.service.DriverLocationService;
import com.learning.logi.graph.api.domain.order.repository.OrderRepository;
import org.springframework.data.geo.Point;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/drivers")
public class DriverController {

    private final DriverLocationService driverLocationService;
    private final OrderRepository orderRepository;

    public DriverController(
            final DriverLocationService driverLocationService,
            final OrderRepository orderRepository
    ) {
        this.driverLocationService = driverLocationService;
        this.orderRepository = orderRepository;
    }

    @PostMapping("/{driverId}/location")
    public ResponseEntity<Void> updateDriverLocation(
            @PathVariable final Long driverId,
            @RequestBody final LocationUpdateRequest request) {

        driverLocationService.updateAndPublishLocation(
                driverId,
                request.latitude(),
                request.longitude()
        );
        return ResponseEntity.noContent().build();
    }
}
