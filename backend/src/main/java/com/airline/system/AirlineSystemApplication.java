package com.airline.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableNeo4jRepositories(basePackages = "com.airline.system.repository")
public class AirlineSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirlineSystemApplication.class, args);
    }
}