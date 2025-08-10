package com.learning.logi.graph.api.domain.route.dto;


import java.util.List;

public record SimpleRouteDTO(List<CoordinatesDTO> coordinates, Double totalDistanceMeters) {  }
