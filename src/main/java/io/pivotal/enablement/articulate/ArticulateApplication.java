package io.pivotal.enablement.articulate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

/**
 * 
 * @author mborges
 *
 */
@SpringBootApplication
@EnableCircuitBreaker
public class ArticulateApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArticulateApplication.class, args);
	}
}
