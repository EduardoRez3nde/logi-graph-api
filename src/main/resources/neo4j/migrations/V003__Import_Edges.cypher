//- MIGRATE IN SEPARATE TRANSACTIONS

CALL {
    LOAD CSV WITH HEADERS FROM 'file:///edges.csv' AS row
    MATCH (start:Intersection {nodeId: toInteger(row.`:START_ID(Intersection)`)})
    MATCH (end:Intersection {nodeId: toInteger(row.`:END_ID(Intersection)`)})
    MERGE (start)-[r:STREET_SEGMENT {distance: toFloat(row.`distance:double`)}]->(end)
} IN TRANSACTIONS OF 1000 ROWS;