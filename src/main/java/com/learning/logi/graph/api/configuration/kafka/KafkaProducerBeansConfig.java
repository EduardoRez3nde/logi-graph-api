package com.learning.logi.graph.api.configuration.kafka;

import com.learning.logi.graph.api.infra.kafka.KafkaProducerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class KafkaProducerBeansConfig {

    @Bean
    public KafkaProducerService<KafkaEvent> kafkaProducerService(final KafkaTemplate<String, KafkaEvent> kafkaTemplate) {
        return new KafkaProducerService<>(kafkaTemplate);
    }
}
