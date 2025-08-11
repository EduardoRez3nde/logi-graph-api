package com.learning.logi.graph.api.infra.kafka;

import com.learning.logi.graph.api.domain.driver.dto.LocationUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerService.class);
    private static final String TOPIC = "driver_location_updates";
    private final KafkaTemplate<String, LocationUpdateEvent> kafkaTemplate;

    public KafkaProducerService(final KafkaTemplate<String, LocationUpdateEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendLocationUpdate(final LocationUpdateEvent event) {
        this.kafkaTemplate.send(TOPIC, String.valueOf(event.deliveryManId()), event);
        LOGGER.info("Evento de localização enviado para o Kafka: {}", event);
    }
}
