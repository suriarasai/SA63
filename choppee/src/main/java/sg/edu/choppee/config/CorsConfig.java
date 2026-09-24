package sg.edu.choppee.config;

// CorsConfig.java  
//
// PURPOSE: Allow Angular (localhost:4200) to call Spring Boot (localhost:8080).
//
// In Thymeleaf, CORS didn't matter because both the HTML and
// the API were served from the same origin (localhost:8080).
// With Angular as a separate frontend (port 4200), we must
// explicitly allow cross-origin requests.



import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins(
                "http://localhost:4200",   // Angular dev server
                "http://localhost:4000",   // Alternative dev port
                "https://your-angular-app.com"  // Production URL
            )
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(false)
            .maxAge(3600);
    }
}
