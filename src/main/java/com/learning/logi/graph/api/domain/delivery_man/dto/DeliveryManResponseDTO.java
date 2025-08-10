package com.learning.logi.graph.api.domain.delivery_man.dto;

import com.learning.logi.graph.api.domain.delivery_man.entities.DeliveryMan;
import com.learning.logi.graph.api.domain.delivery_man.enums.DeliveryManStatus;

public record DeliveryManResponseDTO(
        Long id,
        String name,
        String email,
        DeliveryManStatus status,
        String vehicleLicensePlate
) {
    public static DeliveryManResponseDTO from(
            final Long id,
            final String name,
            final String email,
            final DeliveryManStatus status,
            final String vehicleLicensePlate
    ) {
        return new DeliveryManResponseDTO(id, name, email, status, vehicleLicensePlate);
    }

    public static DeliveryManResponseDTO of(final DeliveryMan deliveryMan) {
        return DeliveryManResponseDTO.from(
                deliveryMan.getId(),
                deliveryMan.getName(),
                deliveryMan.getEmail(),
                deliveryMan.getDeliveryManStatus(),
                deliveryMan.getVehicle().getVehicleLicensePlate()
        );
    }
}
