package sg.edu.iss.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.edu.iss.demo.model.Department;
import sg.edu.iss.demo.model.Employee;
import sg.edu.iss.demo.model.EmploymentType;

public interface EmployeeRepo extends JpaRepository<Employee, Integer> {
	
	/** Employee filter query - notice that the left null is ignored. */
	@Query("""
			select e from Employee e
			where (:name is null or lower(e.name) like lower(concat('%', :name, '%')))
			  and (:dept is null or e.department = :dept)
			  and (:type is null or e.empType = :type)
			order by e.empId""")
	List<Employee> search(@Param("name") String name,
			@Param("dept") Department dept,
			@Param("type") EmploymentType type);

	// Find employees without cubicle
	List<Employee> findByCubicleIsNullOrderByName();

	// Find number of employees without cubicle
	long countByCubicleIsNull();
	
	// Ordered list
	List<Employee> findAllByOrderByName();


}
