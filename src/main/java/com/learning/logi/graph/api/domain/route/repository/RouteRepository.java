package com.learning.logi.graph.api.domain.route.repository;

import com.learning.logi.graph.api.domain.route.dto.DijkstraResult;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RouteRepository extends Neo4jRepository<Object, Long> {

    @Query("MATCH (i:Intersection) " +
            "WITH i, point.distance(i.location, point({longitude: $lon, latitude: $lat})) AS dist " +
            "RETURN id(i) AS nodeId ORDER BY dist ASC LIMIT 1")
    Long findNearestNodeId(@Param("lat") final double lat, @Param("lon") final double lon);

    @Query("MATCH (start: Intersection), (end: Intersection) " +
            "WHERE id(start) = $startVertexId AND id(end) = $endVertexId " +
            "CALL gds.shortestPathDijkstra.stream('my-graph', {" +
            "   sourceNode: start, " +
            "   targetNode: end, " +
            "   relationshipWeightProperty: 'distance' " +
            "}) " +
            "YIELD nodeIds, totalCost " +
            "RETURN nodeIds, totalCost")
    DijkstraResult findShortestPathDijkstra(
            @Param("startVertexId") final Long startVertexId,
            @Param("endVertexId") final Long endVertexId);

    @Query("UNWIND $nodeIds AND nodeId " +
            "MATCH (n: Intersection) WHERE id(n) = nodeId " +
            "RETURN n.location.longitude AS longitude, n.location.latitude AS latitude")
    List<Map<String, Object>> getCoordinatesForNodeIds(@Param("verticesIds") final List<Long> verticesIds);
}
