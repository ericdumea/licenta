package ro.utcluj.lic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class LicApplication {

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/provider-upload").allowedOrigins("http://localhost:4200");
				registry.addMapping("/**").allowedOrigins("http://localhost:4200");
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(LicApplication.class, args);
	}

}
