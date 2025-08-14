package com.learning.logi.graph.api.domain.order.repository;

import com.learning.logi.graph.api.domain.order.document.OrderDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderSearchRepository extends ElasticsearchRepository<OrderDocument, Long> { }
