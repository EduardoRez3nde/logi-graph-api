package com.learning.logi.graph.api.presentation.rest;

import com.learning.logi.graph.api.domain.vehicle.dto.VehicleInsertDTO;
import com.learning.logi.graph.api.domain.vehicle.dto.VehicleResponseDTO;
import com.learning.logi.graph.api.domain.vehicle.dto.VehicleUpdateDTO;
import com.learning.logi.graph.api.domain.vehicle.service.VehicleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(final VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponseDTO> getVehicleById(@PathVariable final Long id) {
        final VehicleResponseDTO vehicleDTO = vehicleService.findById(id);
        return ResponseEntity.ok(vehicleDTO);
    }

    @GetMapping
    public ResponseEntity<Page<VehicleResponseDTO>> findAll(final Pageable pageable) {
        final Page<VehicleResponseDTO> vehicles = vehicleService.findAll(pageable);
        return ResponseEntity.ok(vehicles);
    }

    @PostMapping
    public ResponseEntity<VehicleResponseDTO> createVehicle(@RequestBody final VehicleInsertDTO vehicleInsertDTO) {

        final VehicleResponseDTO createdVehicle = vehicleService.insert(vehicleInsertDTO);

        final URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdVehicle.id())
                .toUri();

        return ResponseEntity.created(uri).body(createdVehicle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponseDTO> update(
            @PathVariable final Long id,
            @RequestBody final VehicleUpdateDTO vehicleDTO) {

        final VehicleResponseDTO response = vehicleService.update(id, vehicleDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable final Long id) {
        vehicleService.delete(id);
    }
}

