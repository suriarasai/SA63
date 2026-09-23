package sg.edu.nus.firstapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import sg.edu.nus.firstapp.model.User;
import sg.edu.nus.firstapp.repo.UserRepository;

@Controller
@RequestMapping("/users")
public class UserController {
	
	 
    private final UserRepository userRepository;
 
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
 
    /** Show the form plus the list of saved users. */
    @GetMapping
    public String showForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }
 
    /**
     * @Valid triggers Bean Validation, which runs @NotBlank, @Email
     * and our custom @ValidPhone. Errors land in BindingResult,
     * which must come immediately after the @Valid parameter.
     */
    @PostMapping
    public String saveUser(@Valid @ModelAttribute("user") User user,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirect) {
        if (result.hasErrors()) {
            // Re-show the form with the error messages and the user's input
            model.addAttribute("users", userRepository.findAll());
            return "users";
        }
        userRepository.save(user);
        redirect.addFlashAttribute("saved", "Saved " + user.getName());
        return "redirect:/users";
    }
}