package com.learning.logi.graph.api.domain.delivery_man.dto;

public record CoordinatesDTO(double latitude, double longitude) {

    public static CoordinatesDTO from(final double latitude, final double longitude) {
        return new CoordinatesDTO(latitude, longitude);
    }
}
