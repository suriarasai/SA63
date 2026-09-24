package sg.edu.iss.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sg.edu.iss.demo.exception.NotFoundException;
import sg.edu.iss.demo.model.Employee;
import sg.edu.iss.demo.model.Gadget;
import sg.edu.iss.demo.repo.GadgetRepo;

/** Gadget owns the FK, so reassigning is a single column update on gadget.emp_id. */
@Service
@RequiredArgsConstructor
public class GadgetService {

	private final GadgetRepo gadgetRepo;
	private final EmployeeService employeeService;

	@Transactional(readOnly = true)
	public List<Gadget> findAll() {
		return gadgetRepo.findAllByOrderByGadgetId();
	}

	@Transactional(readOnly = true)
	public List<Gadget> findUnassigned() {
		return gadgetRepo.findByEmployeeIsNullOrderByName();
	}

	@Transactional
	public Gadget create(Gadget form, Integer ownerId) {
		Gadget g = new Gadget();
		g.setName(form.getName());
		g.setDescription(form.getDescription());
		g = gadgetRepo.save(g);
		if (ownerId != null) {
			employeeService.get(ownerId).addGadget(g);
		}
		return g;
	}

	/** Move a gadget to another employee, or unassign it when empId is null. */
	@Transactional
	public void reassign(Integer gadgetId, Integer empId) {
		Gadget g = get(gadgetId);
		if (empId == null) {
			if (g.getEmployee() != null) {
				g.getEmployee().removeGadget(g);
			}
			return;
		}
		Employee target = employeeService.get(empId);
		target.addGadget(g); // also removes it from the previous owner's list
	}

	@Transactional
	public void delete(Integer gadgetId) {
		Gadget g = get(gadgetId);
		if (g.getEmployee() != null) {
			g.getEmployee().removeGadget(g);
		}
		gadgetRepo.delete(g);
	}

	public Gadget get(Integer id) {
		return gadgetRepo.findById(id)
				.orElseThrow(() -> new NotFoundException("Gadget " + id + " not found"));
	}
}
