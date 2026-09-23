package sg.edu.iss.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.iss.demo.model.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, Integer> {

}
