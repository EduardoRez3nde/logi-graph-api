package com.learning.logi.graph.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication
@EnableCaching
@EnableJpaRepositories(
		basePackages = {
				"com.learning.logi.graph.api.domain.order.repository",
				"com.learning.logi.graph.api.domain.delivery_man.repository",
				"com.learning.logi.graph.api.domain.vehicle.repository"
		},
		entityManagerFactoryRef = "entityManagerFactory",
		transactionManagerRef = "transactionManager"
)
@EnableNeo4jRepositories(
		basePackages = "com.learning.logi.graph.api.domain.route.repository",
		transactionManagerRef = "neo4jTransactionManager"
)
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
