package com.learning.logi.graph.api.domain.route.repository;

import com.learning.logi.graph.api.domain.route.dto.CoordinatesDTO;
import com.learning.logi.graph.api.domain.route.dto.DijkstraResult;
import com.learning.logi.graph.api.domain.route.entity.IntersectionNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RouteRepository extends Neo4jRepository<IntersectionNode, Long> {

    @Query("MATCH (i:Intersection) " +
            "WITH i, point.distance(i.location, point({longitude: $lon, latitude: $lat})) AS dist " +
            "RETURN id(i) AS nodeId ORDER BY dist ASC LIMIT 1")
    Long findNearestNodeId(@Param("lat") final double lat, @Param("lon") final double lon);

    @Query("MATCH (start:Intersection) WHERE id(start) = $startNodeId " +
            "MATCH (end:Intersection) WHERE id(end) = $endNodeId " +
            "CALL gds.shortestPath.dijkstra.stream('my-graph', { " +
            "  sourceNode: start, " +
            "  targetNode: end, " +
            "  relationshipWeightProperty: 'distance' " +
            "}) " +
            "YIELD nodeIds, totalCost " +
            "RETURN nodeIds, totalCost")
    DijkstraResult findShortestPathDijkstra(@Param("startNodeId") Long startNodeId, @Param("endNodeId") Long endNodeId);

    @Query("UNWIND $nodeIds AS nodeId " +
            "MATCH (n:Intersection) WHERE id(n) = nodeId " +
            "RETURN n.location.longitude AS longitude, n.location.latitude AS latitude")
    List<CoordinatesDTO> getCoordinatesForNodeIds(@Param("nodeIds") List<Long> nodeIds);
}
