package sg.edu.iss.demo.controller;

import java.util.ArrayList;
import java.util.List;

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
import sg.edu.iss.demo.exception.BusinessException;
import sg.edu.iss.demo.model.Cubicle;
import sg.edu.iss.demo.model.Department;
import sg.edu.iss.demo.model.Employee;
import sg.edu.iss.demo.model.EmploymentType;
import sg.edu.iss.demo.service.CubicleService;
import sg.edu.iss.demo.service.EmployeeService;
import sg.edu.iss.demo.service.GadgetService;

@Controller
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

	private final EmployeeService employeeService;
	private final CubicleService cubicleService;
	private final GadgetService gadgetService;

	/** Only scalar fields may be bound from a form; associations change through actions. */
	@InitBinder("employee")
	void allowFields(WebDataBinder binder) {
		binder.setAllowedFields("name", "title", "doj", "pay", "department", "empType");
	}

	// ---------- list ----------

	@GetMapping
	public String list(@RequestParam(required = false) String name,
			@RequestParam(required = false) Department dept,
			@RequestParam(required = false) EmploymentType type,
			Model model) {
		model.addAttribute("employees", employeeService.search(name, dept, type));
		model.addAttribute("name", name);
		model.addAttribute("dept", dept);
		model.addAttribute("type", type);
		return "employees/list";
	}

	// ---------- create ----------

	@GetMapping("/new")
	public String newForm(Model model) {
		return form(model, new Employee(), null, null);
	}

	@PostMapping("/new")
	public String create(@Valid @ModelAttribute("employee") Employee employee, BindingResult result,
			@RequestParam(required = false) Integer cubicleId, Model model, RedirectAttributes ra) {
		if (result.hasErrors()) {
			return form(model, employee, cubicleId, null);
		}
		try {
			Employee saved = employeeService.create(employee, cubicleId);
			ra.addFlashAttribute("msg", "Employee " + saved.getName() + " created");
			return "redirect:/employees/" + saved.getEmpId();
		} catch (BusinessException ex) {
			model.addAttribute("error", ex.getMessage());
			return form(model, employee, cubicleId, null);
		}
	}

	// ---------- edit ----------

	@GetMapping("/{id}/edit")
	public String editForm(@PathVariable Integer id, Model model) {
		Employee e = employeeService.findWithAssociations(id);
		Integer cubicleId = e.getCubicle() == null ? null : e.getCubicle().getCubicleId();
		return form(model, e, cubicleId, e.getCubicle());
	}

	@PostMapping("/{id}/edit")
	public String update(@PathVariable Integer id,
			@Valid @ModelAttribute("employee") Employee employee, BindingResult result,
			@RequestParam(required = false) Integer cubicleId, Model model, RedirectAttributes ra) {
		employee.setEmpId(id);
		if (result.hasErrors()) {
			return form(model, employee, cubicleId, employeeService.get(id).getCubicle());
		}
		try {
			employeeService.update(id, employee, cubicleId);
			ra.addFlashAttribute("msg", "Employee " + employee.getName() + " updated");
			return "redirect:/employees/" + id;
		} catch (BusinessException ex) {
			model.addAttribute("error", ex.getMessage());
			return form(model, employee, cubicleId, employeeService.get(id).getCubicle());
		}
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Integer id, RedirectAttributes ra) {
		return Flash.run(ra, "/employees", "Employee deleted", () -> employeeService.delete(id));
	}

	// ---------- detail + association actions ----------

	@GetMapping("/{id}")
	public String detail(@PathVariable Integer id, Model model) {
		model.addAttribute("employee", employeeService.findWithAssociations(id));
		model.addAttribute("freeCubicles", cubicleService.findFree());
		model.addAttribute("unassignedGadgets", gadgetService.findUnassigned());
		model.addAttribute("otherProjects", employeeService.projectsNotJoined(id));
		return "employees/detail";
	}

	@PostMapping("/{id}/cubicle")
	public String moveCubicle(@PathVariable Integer id, @RequestParam Integer cubicleId, RedirectAttributes ra) {
		return Flash.run(ra, "/employees/" + id, "Cubicle assigned",
				() -> employeeService.moveToCubicle(id, cubicleId));
	}

	@PostMapping("/{id}/cubicle/release")
	public String releaseCubicle(@PathVariable Integer id, RedirectAttributes ra) {
		return Flash.run(ra, "/employees/" + id, "Cubicle released",
				() -> employeeService.releaseCubicle(id));
	}

	@PostMapping("/{id}/gadgets")
	public String issueGadget(@PathVariable Integer id, @RequestParam Integer gadgetId, RedirectAttributes ra) {
		return Flash.run(ra, "/employees/" + id, "Gadget issued",
				() -> employeeService.issueGadget(id, gadgetId));
	}

	@PostMapping("/{id}/gadgets/{gadgetId}/unassign")
	public String unassignGadget(@PathVariable Integer id, @PathVariable Integer gadgetId, RedirectAttributes ra) {
		return Flash.run(ra, "/employees/" + id, "Gadget unassigned",
				() -> employeeService.unassignGadget(id, gadgetId));
	}

	@PostMapping("/{id}/projects")
	public String joinProject(@PathVariable Integer id, @RequestParam Integer projectId, RedirectAttributes ra) {
		return Flash.run(ra, "/employees/" + id, "Joined project",
				() -> employeeService.joinProject(id, projectId));
	}

	@PostMapping("/{id}/projects/{projectId}/leave")
	public String leaveProject(@PathVariable Integer id, @PathVariable Integer projectId, RedirectAttributes ra) {
		return Flash.run(ra, "/employees/" + id, "Left project",
				() -> employeeService.leaveProject(id, projectId));
	}

	// ---------- helpers ----------

	/** Free cubicles plus the employee's current one, so it stays selectable on edit. */
	private String form(Model model, Employee employee, Integer selectedCubicleId, Cubicle current) {
		List<Cubicle> choices = new ArrayList<>();
		if (current != null) {
			choices.add(current);
		}
		choices.addAll(cubicleService.findFree());
		model.addAttribute("employee", employee);
		model.addAttribute("cubicleChoices", choices);
		model.addAttribute("selectedCubicleId", selectedCubicleId);
		return "employees/form";
	}
}
