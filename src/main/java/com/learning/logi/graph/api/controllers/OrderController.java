package com.learning.logi.graph.api.controllers;

import com.learning.logi.graph.api.dtos.OrderInsertDTO;
import com.learning.logi.graph.api.dtos.OrderResponseDTO;
import com.learning.logi.graph.api.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
