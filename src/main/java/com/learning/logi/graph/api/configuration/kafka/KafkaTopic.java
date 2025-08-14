package com.learning.logi.graph.api.configuration.kafka;

public enum KafkaTopic {

    DRIVER_LOCATION_UPDATES("driver_location_updates"),
    ORDER_EVENTS("order_events");

    private final String topicName;

    KafkaTopic(final String topicName) {
        this.topicName = topicName;
    }

    public String getTopicName() {
        return topicName;
    }
}
