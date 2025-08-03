package com.learning.logi.graph.api.domain.route.entity;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Intersection")
public class IntersectionNode {
    @Id
    @GeneratedValue
    private Long id;
}