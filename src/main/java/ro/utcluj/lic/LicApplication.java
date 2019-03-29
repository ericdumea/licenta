package ro.utcluj.lic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
public class LicApplication {

	public static void main(String[] args) {
		SpringApplication.run(LicApplication.class, args);
	}

}
