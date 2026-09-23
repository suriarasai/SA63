package sg.edu.nus.firstapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.servlet.http.HttpSession;
import sg.edu.nus.firstapp.dto.LoginUser;

@Controller
public class HomeController {

	@GetMapping("/")
	public String showWelcome(Model model) {
		model.addAttribute("loginuser", new LoginUser());
		return "login";
	}

	@GetMapping("/welcome")
	public String showHome() {
		return "welcome";
	}

	@PostMapping("/authenticate")
	public String authenticate(@ModelAttribute("loginuser") LoginUser loginuser, HttpSession session) {
		if (loginuser.getUsername().equalsIgnoreCase("ghost") && loginuser.getPassword().equalsIgnoreCase("ghost")) {
			session.setAttribute("loginuser", loginuser); 
			return "welcome";
		}
		return "error";
	}
}
