LOAD CSV WITH HEADERS FROM 'file:///vertices.csv' AS row
CREATE (i:Intersection {
    nodeId: toInteger(row.`nodeId:ID(Intersection)`),
    location: point({
        longitude: toFloat(row.`longitude:double`),
        latitude: toFloat(row.`latitude:double`)
    })
});