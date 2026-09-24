package sg.edu.iss.demo.controller;

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
import sg.edu.iss.demo.model.Project;
import sg.edu.iss.demo.service.ProjectService;

@Controller
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

	private final ProjectService projectService;

	@InitBinder("project")
	void allowFields(WebDataBinder binder) {
		binder.setAllowedFields("name", "department", "budget");
	}

	@GetMapping
	public String list(Model model) {
		List<Project> projects = projectService.findAllWithMembers();
		double total = projects.stream()
				.mapToDouble(p -> p.getBudget() == null ? 0 : p.getBudget())
				.sum();
		model.addAttribute("projects", projects);
		model.addAttribute("totalBudget", total);
		return "projects/list";
	}

	@GetMapping("/{id}")
	public String detail(@PathVariable Integer id, Model model) {
		model.addAttribute("project", projectService.findWithMembers(id));
		model.addAttribute("nonMembers", projectService.nonMembers(id));
		return "projects/detail";
	}

	@GetMapping("/new")
	public String newForm(Model model) {
		model.addAttribute("project", new Project());
		return "projects/form";
	}

	@PostMapping("/new")
	public String create(@Valid @ModelAttribute("project") Project project, BindingResult result,
			RedirectAttributes ra) {
		if (result.hasErrors()) {
			return "projects/form";
		}
		Project saved = projectService.create(project);
		ra.addFlashAttribute("msg", "Project " + saved.getName() + " created");
		return "redirect:/projects/" + saved.getProjectId();
	}

	@GetMapping("/{id}/edit")
	public String editForm(@PathVariable Integer id, Model model) {
		model.addAttribute("project", projectService.get(id));
		return "projects/form";
	}

	@PostMapping("/{id}/edit")
	public String update(@PathVariable Integer id, @Valid @ModelAttribute("project") Project project,
			BindingResult result, RedirectAttributes ra) {
		project.setProjectId(id);
		if (result.hasErrors()) {
			return "projects/form";
		}
		projectService.update(id, project);
		ra.addFlashAttribute("msg", "Project " + project.getName() + " updated");
		return "redirect:/projects/" + id;
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Integer id, RedirectAttributes ra) {
		return Flash.run(ra, "/projects", "Project deleted", () -> projectService.delete(id));
	}

	@PostMapping("/{id}/members")
	public String addMember(@PathVariable Integer id, @RequestParam Integer empId, RedirectAttributes ra) {
		return Flash.run(ra, "/projects/" + id, "Member added", () -> projectService.addMember(id, empId));
	}

	@PostMapping("/{id}/members/{empId}/remove")
	public String removeMember(@PathVariable Integer id, @PathVariable Integer empId, RedirectAttributes ra) {
		return Flash.run(ra, "/projects/" + id, "Member removed", () -> projectService.removeMember(id, empId));
	}
}
