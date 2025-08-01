package com.learning.logi.graph.api.infra.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class GeoJsonConverter {

    public static void main(String[] args) throws IOException {

        System.out.println("Iniciando a conversão de GeoJSON para CSV");

        final String resourceName = "map.geojson";
        final InputStream geoJsonFile = GeoJsonConverter.class.getClassLoader().getResourceAsStream(resourceName);

        if (geoJsonFile == null) {
            throw new FileNotFoundException("Arquivo de recurso não encontrado no classpath: " + resourceName);
        }

        final File importDir = new File("import");
        if (!importDir.exists()) {
            if (!importDir.mkdirs())
                throw new RuntimeException("Falha ao criar diretório de importação.");
        }
        final File verticesFile = new File(importDir, "vertices.csv");
        final File edgesFile = new File(importDir, "edges.csv");

        final Map<String, Long> vertices = new HashMap<>();
        long vertexIdCounter = 0;

        try (final PrintWriter verticesWriter = new PrintWriter(new FileWriter(verticesFile));
            final PrintWriter edgesWriter = new PrintWriter(new FileWriter(edgesFile))
        ) {
                verticesWriter.println("nodeId:ID(Intersection),longitude:double,latitude:double,:LABEL");
                edgesWriter.println(":START_ID(Intersection),:END_ID(Intersection),:TYPE");

                final ObjectMapper objectMapper = new ObjectMapper();
                final JsonNode features = objectMapper.readTree(geoJsonFile).path("features");

                System.out.println("Processando " + features.size() + " features do mapa...");

                for (JsonNode feature : features) {

                    final JsonNode geometry = feature.path("geometry");

                    if ("LineString".equals(geometry.path("type").asText())) {

                        final JsonNode coordinates = geometry.path("coordinates");

                        for (int i = 0; i < coordinates.size() - 1; i++) {

                            JsonNode startCoordinate = coordinates.get(i);
                            JsonNode endCoordinate = coordinates.get(i + 1);

                            String startKey = startCoordinate.toString();
                            String endKey = endCoordinate.toString();

                            if (!vertices.containsKey(startKey)) {
                                long vertexId = vertexIdCounter++;
                                vertices.put(startKey, vertexId);
                                verticesWriter.printf("%d,%.7f,%.7f,Intersection\n",
                                        vertexId, startCoordinate.get(0).asDouble(), startCoordinate.get(1).asDouble());
                            }

                            if (!vertices.containsKey(endKey)) {
                                long nodeId = vertexIdCounter++;
                                vertices.put(endKey, nodeId);
                                verticesWriter.printf("%d,%.7f,%.7f,Intersection\n",
                                        nodeId, endCoordinate.get(0).asDouble(), endCoordinate.get(1).asDouble());
                            }
                            long startId = vertices.get(startKey);
                            long endId = vertices.get(endKey);
                            edgesWriter.printf("%d,%d,STREET_SEGMENT\n", startId, endId);
                        }
                    }
                }
            System.out.println("Conversão concluída com sucesso!");
            System.out.println("Total de " + vertexIdCounter + " interseções únicas.");
            System.out.println("Arquivos gerados na pasta '" + importDir.getAbsolutePath() + "'.");
        }
    }
}
