package sg.edu.choppee.controller;

import sg.edu.choppee.model.User;
import sg.edu.choppee.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

/**
 * Session-based authentication: login, register, logout.
 * On successful login, userId and username are stored in the
 * Spring Session-backed HttpSession.
 */
@Controller
public class UserController {

    @Autowired private UserService userService;

    // Login 

    @GetMapping("/login")
    public String loginForm(@RequestParam(required = false) String redirect,
                            Model model) {
        model.addAttribute("redirect", redirect);
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes ra) {

        return userService.login(username, password)
                .map(user -> {
                    session.setAttribute("userId",   user.getId());
                    session.setAttribute("username", user.getUsername());
                    return "redirect:/";
                })
                .orElseGet(() -> {
                    ra.addFlashAttribute("error", "Invalid username or password.");
                    return "redirect:/login";
                });
    }

    //  Register 

    @GetMapping("/register")
    public String registerForm(HttpSession session) {
    	
        session.setAttribute("userId",   "");
        session.setAttribute("username", "");
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam(required = false)
                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate,
                           HttpSession session,
                           RedirectAttributes ra) {
        try {
            User user = new User(username, email, password, firstName, lastName, birthDate);
            User saved = userService.register(user);
            session.setAttribute("userId",   saved.getId());
            session.setAttribute("username", saved.getUsername());
            return "redirect:/";
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/register";
        }
    }

    //  Logout 

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
