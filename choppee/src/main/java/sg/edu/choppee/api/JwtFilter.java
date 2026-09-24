
// JwtFilter.java — Reads JWT from each request header

package sg.edu.choppee.api;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            if (jwtService.isValid(token)) {
                Long userId = jwtService.extractUserId(token);
                // Store userId in request attribute so controllers can read it
                request.setAttribute("authenticatedUserId", userId);
                // Optional Spring Security integration:
                // SecurityContextHolder.getContext().setAuthentication(
                //   new UsernamePasswordAuthenticationToken(userId, null, List.of()));
            }
        }
        chain.doFilter(request, response);
    }
}