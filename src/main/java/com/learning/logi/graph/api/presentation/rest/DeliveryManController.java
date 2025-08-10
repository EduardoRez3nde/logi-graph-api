package com.learning.logi.graph.api.presentation.rest;

import com.learning.logi.graph.api.domain.delivery_man.dto.CoordinatesDTO;
import com.learning.logi.graph.api.domain.delivery_man.dto.DeliveryManInsertDTO;
import com.learning.logi.graph.api.domain.delivery_man.dto.DeliveryManResponseDTO;
import com.learning.logi.graph.api.domain.delivery_man.dto.StatusUpdateDTO;
import com.learning.logi.graph.api.domain.delivery_man.service.DeliveryManService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/delivery-men")
public class DeliveryManController {

    private final DeliveryManService deliveryManService;

    public DeliveryManController(final DeliveryManService deliveryManService) {
        this.deliveryManService = deliveryManService;
    }

    @PostMapping
    public ResponseEntity<DeliveryManResponseDTO> insert(@RequestBody final DeliveryManInsertDTO dto) {

        DeliveryManResponseDTO responseDTO = deliveryManService.insert(dto);

        final URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDTO.id())
                .toUri();

        return ResponseEntity.created(uri).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryManResponseDTO> findById(@PathVariable final Long id) {
        final DeliveryManResponseDTO responseDTO = deliveryManService.findById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/delivery-men-available")
    public ResponseEntity<Page<DeliveryManResponseDTO>> listAvailableDeliveryMen(final Pageable pageable) {
        final Page<DeliveryManResponseDTO> availableMen = deliveryManService.listAvailableDeliveryMen(pageable);
        return ResponseEntity.ok(availableMen);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<DeliveryManResponseDTO> updateStatus(@PathVariable Long id,  @RequestBody final StatusUpdateDTO dto) {
        DeliveryManResponseDTO updatedDeliveryMan = deliveryManService.updateStatus(id, dto.newStatus());
        return ResponseEntity.ok(updatedDeliveryMan);
    }

    @PatchMapping("/{id}/location")
    public ResponseEntity<Void> updateLocation(@PathVariable final Long id, @RequestBody final CoordinatesDTO dto) {
        deliveryManService.updateLocation(id, dto.latitude(), dto.longitude());
        return ResponseEntity.noContent().build();
    }
}
