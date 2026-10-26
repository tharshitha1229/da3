package com.airline.system.controller;

import com.airline.system.model.SqlRequest;
import com.airline.system.util.SqlToCypherTranslator;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.Value;
import org.neo4j.driver.summary.ResultSummary;
import org.neo4j.driver.types.Node;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/sql")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class SqlController {

    private final Driver driver;

    public SqlController(Driver driver) {
        this.driver = driver;
    }

    @PostMapping("/execute")
    public ResponseEntity<Map<String, Object>> executeSQL(@RequestBody SqlRequest request) {
        Map<String, Object> response = new HashMap<>();

        if (request == null || request.getSql() == null || request.getSql().trim().isEmpty()) {
            response.put("success", false);
            response.put("type", "ERROR");
            response.put("error", "No query provided.");
            response.put("message", "Please enter a SQL query first.");
            return ResponseEntity.badRequest().body(response);
        }

        String rawQuery = request.getSql().trim();
        String cypherQuery;
        try {
            cypherQuery = SqlToCypherTranslator.translate(rawQuery);
        } catch (Exception e) {
            cypherQuery = rawQuery;
        }

        response.put("cypher", cypherQuery);
        response.put("translatedCypher", cypherQuery);

        long startTime = System.currentTimeMillis();

        try (Session session = driver.session()) {
            final String queryToRun = cypherQuery;
            Map<String, Object> executionResult = session.executeWrite(tx -> {
                Result result = tx.run(queryToRun);
                List<String> columns = new ArrayList<>(result.keys());
                List<Map<String, Object>> rows = new ArrayList<>();

                boolean unpackedNodes = false;
                List<String> dynamicColumns = new ArrayList<>();

                while (result.hasNext()) {
                    Record record = result.next();
                    Map<String, Object> rowMap = new LinkedHashMap<>();

                    if (columns.size() == 1) {
                        Value singleVal = record.get(0);
                        if (!singleVal.isNull() && "NODE".equalsIgnoreCase(singleVal.type().name())) {
                            Node node = singleVal.asNode();
                            Map<String, Object> nodeMap = node.asMap();
                            for (Map.Entry<String, Object> entry : nodeMap.entrySet()) {
                                rowMap.put(entry.getKey(), entry.getValue());
                                if (!dynamicColumns.contains(entry.getKey())) {
                                    dynamicColumns.add(entry.getKey());
                                }
                            }
                            unpackedNodes = true;
                        } else {
                            rowMap.put(columns.get(0), singleVal.asObject());
                        }
                    } else {
                        for (String key : columns) {
                            Value val = record.get(key);
                            rowMap.put(key, val.isNull() ? null : val.asObject());
                        }
                    }
                    rows.add(rowMap);
                }

                ResultSummary summary = result.consume();
                int affected = 0;
                if (summary != null && summary.counters() != null) {
                    affected = summary.counters().nodesCreated()
                            + summary.counters().nodesDeleted()
                            + summary.counters().relationshipsCreated()
                            + summary.counters().relationshipsDeleted()
                            + summary.counters().propertiesSet();
                }

                Map<String, Object> res = new HashMap<>();
                res.put("columns", unpackedNodes ? dynamicColumns : columns);
                res.put("rows", rows);
                res.put("rowsAffected", affected);
                return res;
            });

            long elapsed = System.currentTimeMillis() - startTime;
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> rows = (List<Map<String, Object>>) executionResult.get("rows");
            @SuppressWarnings("unchecked")
            List<String> columns = (List<String>) executionResult.get("columns");
            int rowsAffected = (int) executionResult.getOrDefault("rowsAffected", 0);

            response.put("success", true);
            response.put("type", "RESULT");
            response.put("columns", columns != null ? columns : Collections.emptyList());
            response.put("rows", rows != null ? rows : Collections.emptyList());
            response.put("rowCount", rows != null ? rows.size() : 0);
            response.put("rowsAffected", rowsAffected);
            response.put("executionTimeMs", elapsed);

            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            long elapsed = System.currentTimeMillis() - startTime;
            response.put("success", false);
            response.put("type", "ERROR");
            response.put("error", ex.getMessage());
            response.put("message", "Neo4j execution error: " + ex.getMessage());
            response.put("executionTimeMs", elapsed);
            return ResponseEntity.ok(response);
        }
    }
}