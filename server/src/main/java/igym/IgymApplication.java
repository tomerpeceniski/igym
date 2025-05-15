package igym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for bootstrapping and launching the IGym Spring Boot application.
 *
 * <p>
 * This class initializes the Spring context and starts the embedded server.
 * </p>
 */
@SpringBootApplication
public class IgymApplication {

	public static void main(String[] args) {
		SpringApplication.run(IgymApplication.class, args);
	}

}
