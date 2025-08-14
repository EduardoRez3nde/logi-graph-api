package com.learning.logi.graph.api.domain.driver.service;

import com.learning.logi.graph.api.configuration.kafka.KafkaEvent;
import com.learning.logi.graph.api.domain.delivery_man.entities.DeliveryMan;
import com.learning.logi.graph.api.domain.driver.dto.LocationUpdateEvent;
import com.learning.logi.graph.api.domain.driver.dto.TrackingResponse;
import com.learning.logi.graph.api.domain.driver.repository.DriverLocationRepository;
import com.learning.logi.graph.api.domain.order.entities.Order;
import com.learning.logi.graph.api.domain.order.repository.OrderRepository;
import com.learning.logi.graph.api.infra.kafka.KafkaProducerService;
import com.learning.logi.graph.api.presentation.exceptions.ResourceNotFoundException;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class DriverLocationService {

    private final DriverLocationRepository driverLocationRepository;
    private final KafkaProducerService<KafkaEvent> kafkaProducerService;
    private final OrderRepository orderRepository;

    public DriverLocationService(
            final DriverLocationRepository driverLocationRepository,
            final KafkaProducerService<KafkaEvent> kafkaProducerService,
            final OrderRepository orderRepository) {
        this.driverLocationRepository = driverLocationRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.orderRepository = orderRepository;
    }

    public void updateAndPublishLocation(final Long driverId, final double latitude, final double longitude) {
        driverLocationRepository.updateLocation(driverId, longitude, latitude);
        final LocationUpdateEvent event = new LocationUpdateEvent(driverId, latitude, longitude, Instant.now());
        kafkaProducerService.sendEvent(event);
    }

    public TrackingResponse getDriverLocationForOrder(final Long orderId) {

        final Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        final DeliveryMan deliveryMan = order.getDeliveryMan();
        if (deliveryMan == null) {
            throw new ResourceNotFoundException("Order is not associated with any delivery person.");
        }

        final Point currentLocation = driverLocationRepository.getLocation(deliveryMan.getId());

        if (currentLocation == null) {
            throw new ResourceNotFoundException("The delivery person's location has not yet been reported.");
        }

        return new TrackingResponse(deliveryMan.getId(), currentLocation.getY(), currentLocation.getX());
    }
}
