package com.learning.logi.graph.api.domain.order.service;

import com.learning.logi.graph.api.domain.order.dto.OrderInsertDTO;
import com.learning.logi.graph.api.domain.order.dto.OrderResponseDTO;
import com.learning.logi.graph.api.domain.order.entities.Order;
import com.learning.logi.graph.api.domain.order.enums.OrderStatus;
import com.learning.logi.graph.api.domain.order.repository.OrderRepository;
import com.learning.logi.graph.api.presentation.exceptions.ResourceNotFoundException;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final GeometryFactory geometryFactory;


    public OrderService(final OrderRepository orderRepository, final GeometryFactory geometryFactory) {
        this.orderRepository = orderRepository;
        this.geometryFactory = geometryFactory;
    }

    @Transactional
    public OrderResponseDTO insert(final OrderInsertDTO orderDTO) {

        if (orderDTO.description() == null) {
            throw new IllegalArgumentException("order description cannot be empty");
        }

        Order order = new Order(
                orderDTO.description(),
                createPoint(orderDTO.collectLon(), orderDTO.collectLat()),
                createPoint(orderDTO.deliveryLon(), orderDTO.deliveryLat())
        );

        order = orderRepository.save(order);

        return OrderResponseDTO.of(order);
    }

    @Transactional(readOnly = true)
    public OrderResponseDTO findById(final Long id) {

        final Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("resource not found with id " + id));

        return OrderResponseDTO.of(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponseDTO> findAllOrdersByStatus(final OrderStatus status, final Pageable pageable) {
        final Page<Order> allOrderByStatus = orderRepository.findByStatus(status.name(), pageable);
        return allOrderByStatus.map(OrderResponseDTO::of);
    }

    private Point createPoint(final double longitude, final double latitude) {

        if (longitude < -180 || longitude > 180 || latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Invalid geographic coordinates.");
        }
        return geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }
}
