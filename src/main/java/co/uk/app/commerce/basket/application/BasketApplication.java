package co.uk.app.commerce.basket.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@SpringBootApplication
@ComponentScan({ "co.uk.app.commerce.basket", "co.uk.app.commerce.additem" })
@EnableMongoRepositories(basePackages = { "co.uk.app.commerce.basket", "co.uk.app.commerce.catalog" })
public class BasketApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(BasketApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BasketApplication.class, args);
		LOGGER.info("Application started successfully");
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}