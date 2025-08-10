package com.learning.logi.graph.api.presentation.rest;

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

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
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
}
