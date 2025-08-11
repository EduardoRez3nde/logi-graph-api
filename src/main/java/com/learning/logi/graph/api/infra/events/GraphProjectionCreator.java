package com.learning.logi.graph.api.infra.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.EventListener;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Component;

@Component
@DependsOn("neo4jMigrationsInitializer")
public class GraphProjectionCreator {

    private final Logger LOGGER = LoggerFactory.getLogger(GraphProjectionCreator.class);
    private final Neo4jClient neo4jClient;

    public GraphProjectionCreator(final Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void graphProjectionCreator() {

        LOGGER.info("Check and verify the GDS 'my-graph' graph projection...");

        final String dropQuery = "CALL gds.graph.drop('my-graph', false) YIELD graphName;";
        final String projectQuery = """
            CALL gds.graph.project(
                'my-graph',
                'Intersection',
                {
                    STREET_SEGMENT: {
                        properties: 'distance'
                    }
                }
            ) YIELD graphName, nodeCount, relationshipCount;
        """;

        try {
            neo4jClient.query(dropQuery).run();
            neo4jClient.query(projectQuery).run();

            LOGGER.info("Graph projection 'my-graph' created successfully.");
        } catch (Exception e) {
            LOGGER.error("Failed to create GDS graph projection: {}", e.getMessage());
        }
    }
}
