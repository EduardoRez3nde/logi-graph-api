package com.learning.logi.graph.api.services;

import com.learning.logi.graph.api.dtos.OrderInsertDTO;
import com.learning.logi.graph.api.dtos.OrderResponseDTO;
import com.learning.logi.graph.api.entities.Order;
import com.learning.logi.graph.api.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final GeometryFactory geometryFactory;


    public OrderService(OrderRepository orderRepository, GeometryFactory geometryFactory) {
        this.orderRepository = orderRepository;
        this.geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
    }

    @Transactional
    public OrderResponseDTO insert(final OrderInsertDTO orderDTO) {

        Order order = new Order(
                orderDTO.description(),
                createPoint(orderDTO.collectLon(), orderDTO.collectLat()),
                createPoint(orderDTO.deliveryLon(), orderDTO.deliveryLat())
        );

        order = orderRepository.save(order);

        return OrderResponseDTO.from(
                order.getId(),
                order.getDescription(),
                order.getOrderStatus().name(),
                order.getCollectionPoint().getX(),
                order.getCollectionPoint().getY(),
                order.getDeliveredPoint().getX(),
                order.getDeliveredPoint().getY(),
                order.getCreatedOn(),
                order.getDeliveryMan().getId()
        );
    }

    private Point createPoint(final double longitude, final double latitude) {

        if (longitude < -180 || longitude > 180 || latitude > -90 || latitude > 90) {
            throw new IllegalArgumentException("Invalid geographic coordinates.");
        }
        return geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }
}
