package com.learning.logi.graph.api.domain.delivery_man.service;

import com.learning.logi.graph.api.domain.delivery_man.dto.DeliveryManInsertDTO;
import com.learning.logi.graph.api.domain.delivery_man.dto.DeliveryManResponseDTO;
import com.learning.logi.graph.api.domain.delivery_man.entities.DeliveryMan;
import com.learning.logi.graph.api.domain.delivery_man.enums.DeliveryManStatus;
import com.learning.logi.graph.api.domain.delivery_man.repository.DeliveryManRepository;
import com.learning.logi.graph.api.domain.vehicle.entities.Vehicle;
import com.learning.logi.graph.api.domain.vehicle.repository.VehicleRepository;
import com.learning.logi.graph.api.presentation.exceptions.ResourceNotFoundException;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeliveryManService {

    private final DeliveryManRepository deliveryManRepository;

    private final VehicleRepository vehicleRepository;

    private final GeometryFactory geometryFactory;

    public DeliveryManService(DeliveryManRepository deliveryManRepository, VehicleRepository vehicleRepository, GeometryFactory geometryFactory) {
        this.deliveryManRepository = deliveryManRepository;
        this.vehicleRepository = vehicleRepository;
        this.geometryFactory = geometryFactory;
    }

    @Transactional
    public DeliveryManResponseDTO insert(final DeliveryManInsertDTO deliveryManInsert) {

        final Vehicle vehicle = vehicleRepository.findById(deliveryManInsert.vehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle with ID " + deliveryManInsert.vehicleId() + " not found."));

        DeliveryMan deliveryMan = DeliveryMan.from(
                deliveryManInsert.name(),
                deliveryManInsert.email(),
                DeliveryManStatus.AVAILABLE,
                vehicle
        );

        deliveryMan = deliveryManRepository.save(deliveryMan);

        return DeliveryManResponseDTO.of(deliveryMan);
    }

    @Transactional(readOnly = true)
    public DeliveryManResponseDTO findById(final Long id) {
        final DeliveryMan deliveryMan = deliveryManRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery person with ID " + id + " not found."));
        return DeliveryManResponseDTO.of(deliveryMan);
    }

    @Transactional(readOnly = true)
    public Page<DeliveryManResponseDTO> listAvailableDeliveryMen(final Pageable pageable) {
        return deliveryManRepository.findByDeliveryManStatus(DeliveryManStatus.AVAILABLE, pageable)
                .map(DeliveryManResponseDTO::of);
    }

    @Transactional
    public DeliveryManResponseDTO updateStatus(final Long id, final DeliveryManStatus newStatus) {

        DeliveryMan deliveryMan = deliveryManRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery person with ID " + id + " not found."));

        // adicionar logica de transição de status
        deliveryMan.setDeliveryManStatus(newStatus);

        deliveryManRepository.save(deliveryMan);
        return DeliveryManResponseDTO.of(deliveryMan);
    }

    @Transactional
    public void updateLocation(final Long id, final double latitude, final double longitude) {

        DeliveryMan deliveryMan = deliveryManRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery person with ID " + id + " not found."));

        deliveryMan.setCurrentLocation(geometryFactory.createPoint(new Coordinate(longitude, latitude)));

        // Salvar tambem no Redis para consultas de tempo real
        // redisTemplate.opsForGeo().add("locations:deliverymen", new Point(longitude, latitude), id.toString());

        deliveryManRepository.save(deliveryMan);
    }
}

