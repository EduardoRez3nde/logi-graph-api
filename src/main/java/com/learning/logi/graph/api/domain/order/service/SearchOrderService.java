package com.learning.logi.graph.api.domain.order.service;

import com.learning.logi.graph.api.domain.order.document.OrderDocument;
import com.learning.logi.graph.api.domain.order.dto.OrderSearchResultDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchOrderService {

    private final ElasticsearchOperations elasticsearchOperations;

    public SearchOrderService(
            final ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public Page<OrderSearchResultDTO> searchOrders(final String queryText, final Pageable pageable) {

        final NativeQuery query = NativeQuery.builder()
                .withQuery(q ->
                        q.multiMatch(mm ->
                                mm.query(queryText)
                                .fields("description", "status")
                        )
                ).build();

        final SearchHits<OrderDocument> searchHits = elasticsearchOperations.search(query, OrderDocument.class);

        final List<OrderSearchResultDTO> results = searchHits.getSearchHits().stream()
                .map(hit -> {
                    final OrderDocument orderDocument = hit.getContent();
                    return OrderSearchResultDTO.from(orderDocument);
                }).toList();

        return new PageImpl<>(results, pageable, searchHits.getTotalHits());
    }
}
