package com.learning.logi.graph.api.domain.delivery_man.mapper;

import com.learning.logi.graph.api.domain.delivery_man.dto.DeliveryManInsertDTO;
import com.learning.logi.graph.api.domain.delivery_man.entities.DeliveryMan;

public class DeliveryManMapper {

    public static void toEntity(final DeliveryMan deliveryMan, final DeliveryManInsertDTO dto) {
        deliveryMan.setName(dto.name());
        deliveryMan.setEmail(dto.email());
    }
}

