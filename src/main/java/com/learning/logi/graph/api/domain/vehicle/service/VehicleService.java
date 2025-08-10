package com.learning.logi.graph.api.domain.vehicle.service;

import com.learning.logi.graph.api.domain.delivery_man.enums.DeliveryManStatus;
import com.learning.logi.graph.api.domain.delivery_man.repository.DeliveryManRepository;
import com.learning.logi.graph.api.domain.vehicle.dto.VehicleInsertDTO;
import com.learning.logi.graph.api.domain.vehicle.dto.VehicleResponseDTO;
import com.learning.logi.graph.api.domain.vehicle.dto.VehicleUpdateDTO;
import com.learning.logi.graph.api.domain.vehicle.entities.Vehicle;
import com.learning.logi.graph.api.domain.vehicle.mapper.VehicleMapper;
import com.learning.logi.graph.api.domain.vehicle.repository.VehicleRepository;
import com.learning.logi.graph.api.presentation.exceptions.LicensePlateAlreadyRegisteredException;
import com.learning.logi.graph.api.presentation.exceptions.VehicleDeletionNotAllowedException;
import com.learning.logi.graph.api.presentation.exceptions.VehicleNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DeliveryManRepository deliveryManRepository;


    public VehicleService(final VehicleRepository vehicleRepository, final DeliveryManRepository deliveryManRepository) {
        this.vehicleRepository = vehicleRepository;
        this.deliveryManRepository = deliveryManRepository;
    }

    @Transactional(readOnly = true)
    public VehicleResponseDTO findById(final Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle with ID " + id + " not found"));
        return VehicleResponseDTO.of(vehicle);
    }

    @Transactional(readOnly = true)
    public Page<VehicleResponseDTO> findAll(final Pageable pageable) {
        return vehicleRepository.findAll(pageable)
                .map(VehicleResponseDTO::of);
    }

    @Transactional
    public VehicleResponseDTO update(final Long id, final VehicleUpdateDTO vehicleDTO) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with id: " + id));

        VehicleMapper.toEntity(vehicle, vehicleDTO);
        vehicle = vehicleRepository.save(vehicle);

        return VehicleResponseDTO.of(vehicle);
    }

    @Transactional
    public VehicleResponseDTO insert(final VehicleInsertDTO vehicleInsert) {

        final Optional<Vehicle> vehicleExists = vehicleRepository.findByVehicleLicensePlate(vehicleInsert.vehicleLicensePlate());

        if (vehicleExists.isPresent()) {
            throw new LicensePlateAlreadyRegisteredException("Plate already registered");
        }
        Vehicle vehicle = VehicleMapper.toEntity(vehicleInsert);
        vehicle = vehicleRepository.save(vehicle);
        return VehicleResponseDTO.of(vehicle);
    }

    @Transactional
    public void delete(final Long id) {
        final Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle with ID " + id + "not found."));

        final boolean inUse = deliveryManRepository.existsByVehicleIdAndDeliveryManStatus(vehicle.getId(), DeliveryManStatus.AVAILABLE);

        if (inUse) {
            throw new VehicleDeletionNotAllowedException("Vehicle is associated with an active delivery person and cannot be removed.");
        }
        vehicleRepository.delete(vehicle);
    }
}
