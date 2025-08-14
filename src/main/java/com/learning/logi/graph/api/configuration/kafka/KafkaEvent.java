package com.learning.logi.graph.api.configuration.kafka;

public interface KafkaEvent {
    KafkaTopic getTopic();
    String getKey();
}
