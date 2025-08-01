package com.learning.logi.graph.api.domain.route.dto;

import java.util.List;

public record RouteDTO(List<CoordinatesDTO> coordinates, Double totalDistanceMeters) {  }
