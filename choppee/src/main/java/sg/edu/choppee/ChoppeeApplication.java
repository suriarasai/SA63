package sg.edu.choppee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Choppee – Spring Boot 3.4.x / Spring 6.x / Java 17 LTS
 *
 * Demonstrates:
 *   - Spring Web MVC
 *   - Spring Data JPA  (all association types)
 *   - Thymeleaf        (minimal tags)
 *   - Spring Session   (in-memory MapSessionRepository)
 *   - H2 Console       → http://localhost:8080/h2-console
 */
@SpringBootApplication
public class ChoppeeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChoppeeApplication.class, args);
    }
}
