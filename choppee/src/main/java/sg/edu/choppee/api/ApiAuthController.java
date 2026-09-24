package sg.edu.choppee.api;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.choppee.model.User;
import sg.edu.choppee.service.UserService;

/**
 * REST equivalent of UserController.
 * Stateless JWT auth — no HttpSession required.
 */
@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    @Autowired private UserService  userService;
    @Autowired private JwtService   jwtService;

    //  DTOs (inner records for brevity) 

    public record LoginRequest(String username, String password) {}
    public record RegisterRequest(
        String username, String email, String password,
        String firstName, String lastName, String birthDate) {}
    public record AuthResponse(String token, Long userId, String username) {}

    // POST /api/auth/login 
    //
    // OLD (UserController):
    //   session.setAttribute("userId",   user.getId());
    //   session.setAttribute("username", user.getUsername());
    //   return "redirect:/";
    //
    // NEW: return JSON { token, userId, username }
    //
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Optional<User> found = userService.login(req.username(), req.password());
        if (found.isEmpty()) {
            return ResponseEntity.status(401)
                .body(Map.of("message", "Invalid username or password."));
        }
        User user = found.get();
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getUsername()));
    }

    //  POST /api/auth/register 
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        try {
            LocalDate bd = req.birthDate() != null && !req.birthDate().isBlank()
                ? LocalDate.parse(req.birthDate()) : null;
            User user = new User(
                req.username(), req.email(), req.password(),
                req.firstName(), req.lastName(), bd
            );
            User saved = userService.register(user);
            String token = jwtService.generateToken(saved);
            return ResponseEntity.ok(
                new AuthResponse(token, saved.getId(), saved.getUsername())
            );
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", ex.getMessage()));
        }
    }
}