package com.learning.logi.graph.api.domain.route.dto;

import java.util.List;

public record DijkstraResult(List<Long> nodeIds, Double totalCost) { }
