package com.learning.logi.graph.api.domain.route.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends Neo4jRepository<Object, Long> {

    @Query("MATCH (i:Intersection) " +
            "WITH i, point.distance(i.location, point({longitude: $lon, latitude: $lat})) AS dist " +
            "RETURN id(i) AS nodeId ORDER BY dist ASC LIMIT 1")
    Long findNearestNodeId(@Param("lat") final double lat, @Param("lon") final double lon);
}
