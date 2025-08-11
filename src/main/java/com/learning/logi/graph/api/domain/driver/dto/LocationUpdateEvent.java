package com.learning.logi.graph.api.domain.driver.dto;

import java.time.Instant;

public record LocationUpdateEvent(Long deliveryManId, double latitude, double longitude, Instant timestamp) { }
