// JwtService.java — Generates & validates JWT tokens
package sg.edu.choppee.api;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import sg.edu.choppee.model.User;

@Service
public class JwtService {

    // In production: load from application.properties (jwt.secret=...)
    private static final String SECRET =
        "choppee-super-secret-key-min-256-bits-for-hs256-algorithm!";
    private static final long   EXPIRY_MS = 86_400_000L; // 24 hours

    private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(User user) {
        return Jwts.builder()
            .setSubject(String.valueOf(user.getId()))
            .claim("username", user.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRY_MS))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    public Long extractUserId(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(key).build()
            .parseClaimsJws(token).getBody();
        return Long.valueOf(claims.getSubject());
    }

    public boolean isValid(String token) {
        try { extractUserId(token); return true; }
        catch (JwtException | IllegalArgumentException e) { return false; }
    }
}
