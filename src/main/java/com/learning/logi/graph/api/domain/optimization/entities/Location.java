package com.learning.logi.graph.api.domain.optimization.entities;

public record Location(Long nodeId, Double latitude, Double longitude) {

    public static Location create(
            final Long nodeId,
            final Double latitude,
            final Double longitude
    ) {
        return new Location(nodeId, latitude, longitude);
    }
}
