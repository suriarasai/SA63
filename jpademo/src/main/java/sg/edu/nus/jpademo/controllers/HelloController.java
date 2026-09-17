package sg.edu.nus.jpademo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import sg.edu.nus.jpademo.model.SampleForm;

@Controller
public class HelloController {
	
	@GetMapping("/hello")
	public String helloMethod(Model model) {
		model.addAttribute("message", "This is my lousy headache controller");		
		return "index";
	}
	
	@GetMapping("/showform")
	public String showForm() {
		return "form";
	}
	
	@PostMapping("/listdata")
	public String processData(Model model) {	
		SampleForm sf  = (SampleForm) model.getAttribute("sampleform");
		model.addAttribute("fname",sf.getFirst_name());
		return "listdata";
	}

}
