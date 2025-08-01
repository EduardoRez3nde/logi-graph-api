package com.learning.logi.graph.api.domain.delivery_man.dto;

public record DeliveryManInsertDTO(
        String name,
        String email,
        Long vehicleId
) {
    public static DeliveryManInsertDTO from(
            final String name,
            final String email,
            final Long vehicleId
    ) {
        return new DeliveryManInsertDTO(name, email, vehicleId);
    }
}
