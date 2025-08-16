package com.learning.logi.graph.api.presentation.rest;

import com.learning.logi.graph.api.domain.order.dto.OrderSearchResultDTO;
import com.learning.logi.graph.api.domain.order.service.SearchOrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchOrderService searchOrderService;

    public SearchController(SearchOrderService searchOrderService) {
        this.searchOrderService = searchOrderService;
    }

    @GetMapping("/orders")
    public ResponseEntity<Page<OrderSearchResultDTO>> searchOrders(
            @RequestParam(name = "query") final String query,
            final Pageable pageable
    ) {
        final Page<OrderSearchResultDTO> results = searchOrderService.searchOrders(query, pageable);
        return ResponseEntity.ok(results);
    }
}
