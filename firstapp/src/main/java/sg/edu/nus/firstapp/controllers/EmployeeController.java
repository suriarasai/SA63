package sg.edu.nus.firstapp.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import sg.edu.nus.firstapp.model.Department;
import sg.edu.nus.firstapp.model.Employee;
import sg.edu.nus.firstapp.model.EmploymentType;
import sg.edu.nus.firstapp.repo.EmployeeRepo;

@Controller
@RequestMapping("/employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeRepo erepo;
	
	@GetMapping("/welcome")
	public String showWelcome() {
		return "welcome";
	}
	
	@GetMapping("/list")
	public String listEmployees(Model model) {
		// Its a silly way of prepopulating
		// come from script (sql) files, Generatortype.IDENTITY not AUTO
		//erepo.deleteAll();
		//Employee e1 = new Employee("Dilbert", LocalDate.of(1975, 1, 1), 3000.00, "software engineer", EmploymentType.PERMEMPLOYEE, Department.PRODUCT);
		//Employee e2 = new Employee("Alice", LocalDate.of(1978, 1, 1), 3000.00, "software engineer", EmploymentType.PERMEMPLOYEE, Department.PRODUCT);
        //erepo.save(e1); erepo.save(e2); 
		model.addAttribute("employees", erepo.findAll());
		return "listemployees";
	}
	
	@GetMapping("/showempform")
	public String showEmployeeForm(Model model) {
		Employee employee = new Employee();
		model.addAttribute("employee", employee);
		return "empform";
	}
	
	@PostMapping("/save")
	public String saveEmployee(Model model, @ModelAttribute Employee employee) {
		erepo.save(employee);
		return "redirect:/employee/list";
		
	}

}
