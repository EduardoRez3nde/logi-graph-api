package com.learning.logi.graph.api.domain.driver.dto;

import com.learning.logi.graph.api.configuration.kafka.KafkaEvent;
import com.learning.logi.graph.api.configuration.kafka.KafkaTopic;

import java.time.Instant;

public record LocationUpdateEvent(Long deliveryManId, double latitude, double longitude, Instant timestamp) implements KafkaEvent {

    @Override
    public KafkaTopic getTopic() {
        return  KafkaTopic.DRIVER_LOCATION_UPDATES;
    }

    @Override
    public String getKey() {
        return String.valueOf(this.deliveryManId);
    }
}
