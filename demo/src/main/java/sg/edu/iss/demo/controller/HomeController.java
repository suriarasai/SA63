package sg.edu.iss.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import sg.edu.iss.demo.repo.CubicleRepo;
import sg.edu.iss.demo.repo.EmployeeRepo;
import sg.edu.iss.demo.repo.GadgetRepo;
import sg.edu.iss.demo.repo.ProjectRepo;

@Controller
@RequiredArgsConstructor
public class HomeController {

	private final EmployeeRepo employeeRepo;
	private final CubicleRepo cubicleRepo;
	private final ProjectRepo projectRepo;
	private final GadgetRepo gadgetRepo;

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("employeeCount", employeeRepo.count());
		model.addAttribute("employeesWithoutCubicle", employeeRepo.countByCubicleIsNull());
		model.addAttribute("cubicleCount", cubicleRepo.count());
		model.addAttribute("freeCubicles", cubicleRepo.countFree());
		model.addAttribute("projectCount", projectRepo.count());
		model.addAttribute("projectsWithoutMembers", projectRepo.countWithoutMembers());
		model.addAttribute("gadgetCount", gadgetRepo.count());
		model.addAttribute("unassignedGadgets", gadgetRepo.countByEmployeeIsNull());
		return "home";
	}
}
