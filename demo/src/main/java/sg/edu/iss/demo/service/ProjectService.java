package sg.edu.iss.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sg.edu.iss.demo.exception.NotFoundException;
import sg.edu.iss.demo.model.Employee;
import sg.edu.iss.demo.model.Project;
import sg.edu.iss.demo.repo.EmployeeRepo;
import sg.edu.iss.demo.repo.ProjectRepo;

/** Project-side functions. 
 * Employee owns the join table, so membership changes go through EmployeeService. */
@Service
@RequiredArgsConstructor
public class ProjectService {

	private final ProjectRepo projectRepo;
	private final EmployeeRepo employeeRepo;
	private final EmployeeService employeeService;

	@Transactional(readOnly = true)
	public List<Project> findAllWithMembers() {
		List<Project> list = projectRepo.findAllByOrderByProjectId();
		list.forEach(p -> Hibernate.initialize(p.getEmployees()));
		return list;
	}

	@Transactional(readOnly = true)
	public Project findWithMembers(Integer id) {
		Project p = get(id);
		Hibernate.initialize(p.getEmployees());
		return p;
	}

	@Transactional(readOnly = true)
	public List<Employee> nonMembers(Integer projectId) {
		Project p = get(projectId);
		List<Employee> all = new ArrayList<>(employeeRepo.findAllByOrderByName());
		all.removeAll(p.getEmployees());
		return all;
	}

	@Transactional
	public Project create(Project form) {
		Project p = new Project();
		copyFields(form, p);
		return projectRepo.save(p);
	}

	@Transactional
	public void update(Integer id, Project form) {
		copyFields(form, get(id));
	}

	/** Remove the project from every member's list first.
	 * Then  join rows belong to Employee. */
	@Transactional
	public void delete(Integer id) {
		Project p = get(id);
		for (Employee e : new ArrayList<>(p.getEmployees())) {
			e.leaveProject(p);
		}
		projectRepo.delete(p);
	}

	@Transactional
	public void addMember(Integer projectId, Integer empId) {
		employeeService.joinProject(empId, projectId);
	}

	@Transactional
	public void removeMember(Integer projectId, Integer empId) {
		employeeService.leaveProject(empId, projectId);
	}

	public Project get(Integer id) {
		return projectRepo.findById(id)
				.orElseThrow(() -> new NotFoundException("Project " + id + " not found"));
	}

	private void copyFields(Project from, Project to) {
		to.setName(from.getName());
		to.setDepartment(from.getDepartment());
		to.setBudget(from.getBudget());
	}
}
