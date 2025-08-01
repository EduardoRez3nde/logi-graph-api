package com.learning.logi.graph.api.domain.delivery_man.dto;

import com.learning.logi.graph.api.domain.delivery_man.enums.DeliveryManStatus;

public record StatusUpdateDTO(DeliveryManStatus newStatus) {
}
