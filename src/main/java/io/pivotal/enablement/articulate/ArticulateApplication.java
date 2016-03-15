package io.pivotal.enablement.articulate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @author mborges
 *
 */
@SpringBootApplication
@Configuration
public class ArticulateApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArticulateApplication.class, args);
	}
}
