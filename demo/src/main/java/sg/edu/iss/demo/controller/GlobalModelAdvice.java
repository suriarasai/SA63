package sg.edu.iss.demo.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import sg.edu.iss.demo.model.Department;
import sg.edu.iss.demo.model.EmploymentType;

/** Enum values for every drop-down, available to all templates. */
@ControllerAdvice
public class GlobalModelAdvice {

	@ModelAttribute("departments")
	public Department[] departments() {
		return Department.values();
	}

	@ModelAttribute("employmentTypes")
	public EmploymentType[] employmentTypes() {
		return EmploymentType.values();
	}
}
