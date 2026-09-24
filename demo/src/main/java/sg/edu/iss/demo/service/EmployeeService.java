package sg.edu.iss.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sg.edu.iss.demo.exception.BusinessException;
import sg.edu.iss.demo.exception.NotFoundException;
import sg.edu.iss.demo.model.Cubicle;
import sg.edu.iss.demo.model.Department;
import sg.edu.iss.demo.model.Employee;
import sg.edu.iss.demo.model.EmploymentType;
import sg.edu.iss.demo.model.Gadget;
import sg.edu.iss.demo.model.Project;
import sg.edu.iss.demo.repo.CubicleRepo;
import sg.edu.iss.demo.repo.EmployeeRepo;
import sg.edu.iss.demo.repo.GadgetRepo;
import sg.edu.iss.demo.repo.ProjectRepo;

/**
 * All Employee-side business functions. Every change goes through the entity
 * helpers so both ends of each bidirectional link stay consistent.
 *
 * open-in-view is false, so lazy lists (gadgets, projects) are initialised here,
 * inside the transaction, before the entity reaches a template.
 */
@Service
@RequiredArgsConstructor
public class EmployeeService {

	private final EmployeeRepo employeeRepo;
	private final CubicleRepo cubicleRepo;
	private final GadgetRepo gadgetRepo;
	private final ProjectRepo projectRepo;

	// ---------------- queries ----------------

	@Transactional(readOnly = true)
	public List<Employee> search(String name, Department dept, EmploymentType type) {
		String n = (name == null || name.isBlank()) ? null : name.trim();
		List<Employee> list = employeeRepo.search(n, dept, type);
		list.forEach(this::initAssociations);
		return list;
	}

	@Transactional(readOnly = true)
	public Employee findWithAssociations(Integer id) {
		Employee e = get(id);
		initAssociations(e);
		return e;
	}

	@Transactional(readOnly = true)
	public List<Employee> findAll() {
		return employeeRepo.findAllByOrderByName();
	}

	@Transactional(readOnly = true)
	public List<Employee> findWithoutCubicle() {
		return employeeRepo.findByCubicleIsNullOrderByName();
	}

	/** Projects this employee has not joined yet (for the "Join project" list). */
	@Transactional(readOnly = true)
	public List<Project> projectsNotJoined(Integer empId) {
		Employee e = get(empId);
		List<Project> all = new ArrayList<>(projectRepo.findAllByOrderByProjectId());
		all.removeAll(e.getProjects());
		return all;
	}

	// Employee CRUD

	@Transactional
	public Employee create(Employee form, Integer cubicleId) {
		Employee e = new Employee();
		copyFields(form, e);
		e = employeeRepo.save(e);
		if (cubicleId != null) {
			e.assignCubicle(freeCubicleFor(e, cubicleId));
		}
		return e;
	}

	/**
	 * Only scalar fields are copied from the form. Saving the detached form object
	 * directly would overwrite the projects list with an empty one and delete the
	 * employee's join-table rows.
	 */
	@Transactional
	public void update(Integer id, Employee form, Integer cubicleId) {
		Employee e = get(id);
		copyFields(form, e);
		Integer current = e.getCubicle() == null ? null : e.getCubicle().getCubicleId();
		if (cubicleId == null) {
			e.assignCubicle(null);
		} else if (!cubicleId.equals(current)) {
			e.assignCubicle(freeCubicleFor(e, cubicleId));
		}
	}

	/** Releases the cubicle, un-issues gadgets and leaves projects, then deletes. */
	@Transactional
	public void delete(Integer id) {
		Employee e = get(id);
		e.assignCubicle(null);
		for (Gadget g : new ArrayList<>(e.getGadgets())) {
			e.removeGadget(g);
		}
		for (Project p : new ArrayList<>(e.getProjects())) {
			e.leaveProject(p);
		}
		employeeRepo.delete(e);
	}

	// Cubicle Related

	@Transactional
	public void moveToCubicle(Integer empId, Integer cubicleId) {
		Employee e = get(empId);
		e.assignCubicle(freeCubicleFor(e, cubicleId));
	}

	@Transactional
	public void releaseCubicle(Integer empId) {
		Employee e = get(empId);
		if (e.getCubicle() == null) {
			throw new BusinessException(e.getName() + " has no cubicle to release");
		}
		e.assignCubicle(null);
	}

	// Gadget Related

	@Transactional
	public void issueGadget(Integer empId, Integer gadgetId) {
		Employee e = get(empId);
		Gadget g = gadgetRepo.findById(gadgetId)
				.orElseThrow(() -> new NotFoundException("Gadget " + gadgetId + " not found"));
		if (g.getEmployee() != null) {
			throw new BusinessException(g.getName() + " is already issued to " + g.getEmployee().getName());
		}
		e.addGadget(g);
	}

	@Transactional
	public void unassignGadget(Integer empId, Integer gadgetId) {
		Employee e = get(empId);
		Gadget g = gadgetRepo.findById(gadgetId)
				.orElseThrow(() -> new NotFoundException("Gadget " + gadgetId + " not found"));
		if (g.getEmployee() != e) {
			throw new BusinessException(g.getName() + " is not issued to " + e.getName());
		}
		e.removeGadget(g);
	}

	// Project Related

	@Transactional
	public void joinProject(Integer empId, Integer projectId) {
		Employee e = get(empId);
		Project p = project(projectId);
		if (e.getProjects().contains(p)) {
			throw new BusinessException(e.getName() + " is already on " + p.getName());
		}
		e.joinProject(p);
	}

	@Transactional
	public void leaveProject(Integer empId, Integer projectId) {
		Employee e = get(empId);
		Project p = project(projectId);
		if (!e.getProjects().contains(p)) {
			throw new BusinessException(e.getName() + " is not on " + p.getName());
		}
		e.leaveProject(p);
	}

	// Additional

	public Employee get(Integer id) {
		return employeeRepo.findById(id)
				.orElseThrow(() -> new NotFoundException("Employee " + id + " not found"));
	}

	private Project project(Integer id) {
		return projectRepo.findById(id)
				.orElseThrow(() -> new NotFoundException("Project " + id + " not found"));
	}

	private Cubicle freeCubicleFor(Employee e, Integer cubicleId) {
		Cubicle c = cubicleRepo.findById(cubicleId)
				.orElseThrow(() -> new NotFoundException("Cubicle " + cubicleId + " not found"));
		if (c.getEmployee() != null && c.getEmployee() != e) {
			throw new BusinessException(c.getName() + " is already occupied by " + c.getEmployee().getName());
		}
		return c;
	}

	private void copyFields(Employee from, Employee to) {
		to.setName(from.getName());
		to.setTitle(from.getTitle());
		to.setDoj(from.getDoj());
		to.setPay(from.getPay());
		to.setDepartment(from.getDepartment());
		to.setEmpType(from.getEmpType());
	}

	private void initAssociations(Employee e) {
		Hibernate.initialize(e.getGadgets());
		Hibernate.initialize(e.getProjects());
	}
}
