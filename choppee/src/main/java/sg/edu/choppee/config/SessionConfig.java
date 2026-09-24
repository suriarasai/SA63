package sg.edu.choppee.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;

import java.util.concurrent.ConcurrentHashMap;

import java.time.Duration;

/**
 * Spring Session – backs HTTP sessions with an in-memory ConcurrentHashMap.
 * Drop-in replacement: swap MapSessionRepository for RedisIndexedSessionRepository
 * in a production environment without changing any controller code.
 */
@Configuration
@EnableSpringHttpSession
public class SessionConfig {

    @Bean
    public MapSessionRepository sessionRepository() {
        MapSessionRepository repo = new MapSessionRepository(new ConcurrentHashMap<>());
        repo.setDefaultMaxInactiveInterval(Duration.ofMinutes(30));// 30 minutes
        return repo;
    }
}
