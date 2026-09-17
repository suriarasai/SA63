package sg.edu.nus.jpademo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	public String showForm(Model model) {
		//Create an empty spring bean
		SampleForm sample = new SampleForm();
		model.addAttribute(sample);
		return "form";
	}
	
	@PostMapping("/listdata")
	public String processData(Model model, @ModelAttribute SampleForm sample) {	
		model.addAttribute(sample);
		return "listdata";
	}

}
