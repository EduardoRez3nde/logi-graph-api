package com.learning.logi.graph.api.domain.order.service;

import com.learning.logi.graph.api.configuration.kafka.KafkaEvent;
import com.learning.logi.graph.api.domain.order.document.OrderDocument;
import com.learning.logi.graph.api.domain.order.dto.OrderUpdateEvent;
import com.learning.logi.graph.api.domain.order.repository.OrderSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderIndexerService {

    private final Logger LOGGER = LoggerFactory.getLogger(OrderIndexerService.class);
    private final OrderSearchRepository orderSearchRepository;

    public OrderIndexerService(
            final OrderSearchRepository orderSearchRepository
    ) {
        this.orderSearchRepository = orderSearchRepository;
    }

    @KafkaListener(topics = "order_events", groupId = "order-indexer-group")
    public void consumeOrderEvent(final OrderUpdateEvent event) {

        LOGGER.info("Recebido o evento do pedido {} para indexação.", event.getKey());

        try {
            final OrderDocument document = OrderDocument.from(event);
            orderSearchRepository.save(document);

            LOGGER.info("Pedido {} indexado com sucesso no ElasticSearch.", event.id());
        } catch (Exception e) {
            LOGGER.info("Falha ao indezar o pedido {}: {}", event.id(), e.getMessage());
            // TODO: logica dead letter queue. usar DefaultErrorHandler
        }
    }
}
