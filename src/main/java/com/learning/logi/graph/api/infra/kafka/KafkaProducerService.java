package com.learning.logi.graph.api.infra.kafka;

import com.learning.logi.graph.api.configuration.kafka.KafkaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaProducerService<T extends KafkaEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducerService.class);
    private final KafkaTemplate<String, T> kafkaTemplate;

    public KafkaProducerService(final KafkaTemplate<String, T> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(T event) {
        try {
            final String topicName = event.getTopic().getTopicName();
            this.kafkaTemplate.send(topicName, event.getKey(), event);
            LOGGER.info("Evento enviado com sucesso para o tópico '{}': {}", topicName, event);
        } catch (Exception e) {
            LOGGER.error("Falha ao enviar evento para o tópico '{}'", event.getTopic(), e);
        }
    }
}
