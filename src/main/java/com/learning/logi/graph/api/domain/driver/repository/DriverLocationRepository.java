package com.learning.logi.graph.api.domain.driver.repository;

import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DriverLocationRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String KEY = "driver_locations";

    public DriverLocationRepository(final RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void updateLocation(final Long deliveryManId, final double longitude, final double latitude) {
        redisTemplate.opsForGeo().add(
                KEY,
                new Point(longitude, latitude),
                String.valueOf(deliveryManId)
        );
    }

    public Point getLocation(final Long deliveryManId) {
        final List<Point> locations = redisTemplate.opsForGeo().position(KEY, String.valueOf(deliveryManId));
        return (locations != null && !locations.isEmpty()) ? locations.getFirst() : null;
    }
}
