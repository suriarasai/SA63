package sg.edu.nus.jpademo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
	
	@GetMapping("/hello")
	public String helloMethod(Model model) {
		model.addAttribute("message", "This is my lousy headache controller");		
		return "index";
	}
	
	@GetMapping("/showform")
	public String showForm() {
		return "from";
	}

}
