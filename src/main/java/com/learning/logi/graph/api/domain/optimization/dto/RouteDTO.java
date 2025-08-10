package com.learning.logi.graph.api.domain.optimization.dto;

import com.learning.logi.graph.api.domain.route.dto.CoordinatesDTO;

import java.util.List;

public record RouteDTO(Long deliveryManId, List<CoordinatesDTO> coordinates, Double totalDistanceMeters) {  }
