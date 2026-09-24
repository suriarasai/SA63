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
import sg.edu.iss.demo.model.Gadget;
import sg.edu.iss.demo.service.EmployeeService;
import sg.edu.iss.demo.service.GadgetService;

@Controller
@RequestMapping("/gadgets")
@RequiredArgsConstructor
public class GadgetController {

	private final GadgetService gadgetService;
	private final EmployeeService employeeService;

	@InitBinder("gadget")
	void allowFields(WebDataBinder binder) {
		binder.setAllowedFields("name", "description");
	}

	@GetMapping
	public String list(Model model) {
		return listView(model, new Gadget(), null, false);
	}

	@PostMapping
	public String create(@Valid @ModelAttribute("gadget") Gadget gadget, BindingResult result,
			@RequestParam(required = false) Integer ownerId, Model model, RedirectAttributes ra) {
		if (result.hasErrors()) {
			return listView(model, gadget, ownerId, true);
		}
		gadgetService.create(gadget, ownerId);
		ra.addFlashAttribute("msg", "Gadget " + gadget.getName() + " added");
		return "redirect:/gadgets";
	}

	@PostMapping("/{id}/owner")
	public String reassign(@PathVariable Integer id, @RequestParam(required = false) Integer empId,
			RedirectAttributes ra) {
		return Flash.run(ra, "/gadgets", "Owner updated", () -> gadgetService.reassign(id, empId));
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Integer id, RedirectAttributes ra) {
		return Flash.run(ra, "/gadgets", "Gadget deleted", () -> gadgetService.delete(id));
	}

	private String listView(Model model, Gadget form, Integer ownerId, boolean showAddForm) {
		model.addAttribute("gadgets", gadgetService.findAll());
		model.addAttribute("employees", employeeService.findAll());
		model.addAttribute("gadget", form);
		model.addAttribute("ownerId", ownerId);
		model.addAttribute("showAddForm", showAddForm);
		return "gadgets/list";
	}
}
