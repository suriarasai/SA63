package sg.edu.nus.backend.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.backend.model.Employee;
import sg.edu.nus.backend.repository.EmployeeRepository;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "http://localhost:4200") // Angular 4200
public class EmployeeController {
	@Autowired
	EmployeeRepository empRepo;

	//  GET All Employees 
	@GetMapping
	public ResponseEntity<List<Employee>> getAllEmployees() {

		return ResponseEntity.ok(empRepo.findAll());
	}

	// GET Employee by ID 
	@GetMapping("/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {

		Optional<Employee> employee = empRepo.findById(id);

		return employee.map(ResponseEntity::ok).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	//  POST Create Employee 
	@PostMapping
	public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {

		empRepo.save(employee);
		return ResponseEntity.status(HttpStatus.CREATED).body(employee);
	}

	// PUT Update Employee 
	@PutMapping("/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmployee) {
		
		return empRepo.findById(id).map(existing -> {
            //updatedEmployee.setId(id);
            return ResponseEntity.ok(empRepo.save(updatedEmployee));
        }).orElse(ResponseEntity.notFound().build());

		
	}

	// DELETE Employee 
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
		Optional<Employee> employee = empRepo.findById(id);
		if (employee.isPresent()) {
			empRepo.delete(employee.get());
			return ResponseEntity.ok("Employee with ID " + id + " deleted successfully.");
		} else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found.");

	}
}
