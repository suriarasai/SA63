package sg.edu.iss.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sg.edu.iss.demo.model.Cubicle;
import sg.edu.iss.demo.repo.CubicleRepo;
import sg.edu.iss.demo.service.CubicleService;
import sg.edu.iss.demo.service.EmployeeService;

@Controller
@RequestMapping("/cubicles")
@RequiredArgsConstructor
public class CubicleController {

	private final CubicleService cubicleService;
	private final EmployeeService employeeService;
	private final CubicleRepo cubicleRepo;

	@InitBinder("cubicle")
	void allowFields(WebDataBinder binder) {
		binder.setAllowedFields("name", "location", "description");
	}

	@GetMapping
	public String list(@RequestParam(defaultValue = "all") String filter, Model model) {
		long total = cubicleRepo.count();
		long free = cubicleRepo.countFree();
		model.addAttribute("cubicles", cubicleService.list(filter));
		model.addAttribute("filter", filter);
		model.addAttribute("total", total);
		model.addAttribute("free", free);
		model.addAttribute("occupied", total - free);
		model.addAttribute("unseated", employeeService.findWithoutCubicle());
		return "cubicles/list";
	}

	@GetMapping("/new")
	public String newForm(Model model) {
		model.addAttribute("cubicle", new Cubicle());
		return "cubicles/form";
	}

	@PostMapping("/new")
	public String create(@Valid @ModelAttribute("cubicle") Cubicle cubicle, BindingResult result,
			RedirectAttributes ra) {
		if (result.hasErrors()) {
			return "cubicles/form";
		}
		cubicleService.create(cubicle);
		ra.addFlashAttribute("msg", "Cubicle " + cubicle.getName() + " created");
		return "redirect:/cubicles";
	}

	@GetMapping("/{id}/edit")
	public String editForm(@PathVariable Integer id, Model model) {
		model.addAttribute("cubicle", cubicleService.get(id));
		return "cubicles/form";
	}

	@PostMapping("/{id}/edit")
	public String update(@PathVariable Integer id, @Valid @ModelAttribute("cubicle") Cubicle cubicle,
			BindingResult result, RedirectAttributes ra) {
		cubicle.setCubicleId(id);
		if (result.hasErrors()) {
			return "cubicles/form";
		}
		cubicleService.update(id, cubicle);
		ra.addFlashAttribute("msg", "Cubicle " + cubicle.getName() + " updated");
		return "redirect:/cubicles";
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Integer id, RedirectAttributes ra) {
		return Flash.run(ra, "/cubicles", "Cubicle deleted", () -> cubicleService.delete(id));
	}

	@PostMapping("/{id}/assign")
	public String assign(@PathVariable Integer id, @RequestParam Integer empId, RedirectAttributes ra) {
		return Flash.run(ra, "/cubicles", "Cubicle assigned", () -> cubicleService.assign(id, empId));
	}

	@PostMapping("/{id}/vacate")
	public String vacate(@PathVariable Integer id, RedirectAttributes ra) {
		return Flash.run(ra, "/cubicles", "Cubicle vacated", () -> cubicleService.vacate(id));
	}
}
