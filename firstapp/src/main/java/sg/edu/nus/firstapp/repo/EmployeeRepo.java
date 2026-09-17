package sg.edu.nus.firstapp.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.firstapp.model.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, Integer>{

}
