package com.learning.logi.graph.api.configuration.kafka;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public KafkaAdmin.NewTopics topics() {
        return new KafkaAdmin.NewTopics(
                TopicBuilder
                        .name("driver_location_updates")
                        .partitions(1)
                        .replicas(1)
                        .compact()
                        .build(),
                TopicBuilder
                        .name("order_events")
                        .partitions(1)
                        .compact()
                        .build()
        );
    }
}

