package sg.edu.nus.mappingdemo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import sg.edu.nus.mappingdemo.repo.BiEmployeeRepo;

@Controller
public class BiDirectionalController {
	
	@Autowired
	BiEmployeeRepo biempRepo;
	
	@GetMapping("testquery")
	public String printEmp(Model model) {
		model.addAttribute("elist",biempRepo.findByTwoArgumentsByParam(1, "D"));
		//model.addAttribute("elist",biempRepo.findByArgument(3));
		return "elist";
		
	}
	

}
