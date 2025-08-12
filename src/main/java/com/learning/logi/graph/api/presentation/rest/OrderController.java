package com.learning.logi.graph.api.presentation.rest;

import com.learning.logi.graph.api.domain.driver.dto.TrackingResponse;
import com.learning.logi.graph.api.domain.driver.service.DriverLocationService;
import com.learning.logi.graph.api.domain.order.dto.OrderInsertDTO;
import com.learning.logi.graph.api.domain.order.dto.OrderResponseDTO;
import com.learning.logi.graph.api.domain.order.enums.OrderStatus;
import com.learning.logi.graph.api.domain.order.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final DriverLocationService driverLocationService;

    public OrderController(
            final OrderService orderService,
            final DriverLocationService driverLocationService
    ) {
        this.orderService = orderService;
        this.driverLocationService = driverLocationService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> insert(@RequestBody final OrderInsertDTO orderDTO) {

        final OrderResponseDTO result = orderService.insert(orderDTO);

        final URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.id())
                .toUri();

        return ResponseEntity.created(uri).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> findById(@PathVariable final Long id) {
        final OrderResponseDTO order = orderService.findById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> findAllOrdersByStatus(
            @RequestParam final String status,
            final Pageable pageable
    ) {
        final Page<OrderResponseDTO> allOrderByStatus = orderService.findAllOrdersByStatus(OrderStatus.valueOf(status), pageable);
        return ResponseEntity.ok(allOrderByStatus);
    }


    @GetMapping("/{orderId}/track")
    public ResponseEntity<TrackingResponse> trackOrder(@PathVariable final Long orderId) {

        final TrackingResponse response = driverLocationService.getDriverLocationForOrder(orderId);

        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }
}
