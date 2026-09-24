package sg.edu.iss.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sg.edu.iss.demo.exception.BusinessException;
import sg.edu.iss.demo.exception.NotFoundException;
import sg.edu.iss.demo.model.Cubicle;
import sg.edu.iss.demo.repo.CubicleRepo;

/** Cubicle-side functions. 
 * Employee owns the FK, so links are changed via EmployeeService. */
@Service
@RequiredArgsConstructor
public class CubicleService {

	private final CubicleRepo cubicleRepo;
	private final EmployeeService employeeService;

	@Transactional(readOnly = true)
	public List<Cubicle> list(String filter) {
		return switch (filter == null ? "all" : filter) {
			case "free" -> cubicleRepo.findFree();
			case "occupied" -> cubicleRepo.findOccupied();
			default -> cubicleRepo.findAllByOrderByName();
		};
	}

	@Transactional(readOnly = true)
	public List<Cubicle> findFree() {
		return cubicleRepo.findFree();
	}

	@Transactional(readOnly = true)
	public Cubicle get(Integer id) {
		return cubicleRepo.findById(id)
				.orElseThrow(() -> new NotFoundException("Cubicle " + id + " not found"));
	}

	@Transactional
	public Cubicle create(Cubicle form) {
		Cubicle c = new Cubicle();
		copyFields(form, c);
		return cubicleRepo.save(c);
	}

	@Transactional
	public void update(Integer id, Cubicle form) {
		copyFields(form, get(id));
	}

	@Transactional
	public void delete(Integer id) {
		Cubicle c = get(id);
		if (!c.isFree()) {
			throw new BusinessException(c.getName() + " is occupied by " + c.getEmployee().getName()
					+ ". Vacate it first.");
		}
		cubicleRepo.delete(c);
	}

	/** Assign from the cubicle side: the change is written on the owning Employee. */
	@Transactional
	public void assign(Integer cubicleId, Integer empId) {
		employeeService.moveToCubicle(empId, cubicleId);
	}

	@Transactional
	public void vacate(Integer cubicleId) {
		Cubicle c = get(cubicleId);
		if (c.isFree()) {
			throw new BusinessException(c.getName() + " is already free");
		}
		c.getEmployee().assignCubicle(null);
	}

	private void copyFields(Cubicle from, Cubicle to) {
		to.setName(from.getName());
		to.setLocation(from.getLocation());
		to.setDescription(from.getDescription());
	}
}
