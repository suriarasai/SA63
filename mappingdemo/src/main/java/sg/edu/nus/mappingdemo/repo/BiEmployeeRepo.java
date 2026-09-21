package sg.edu.nus.mappingdemo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.mappingdemo.model.bi.BiEmployee;

public interface BiEmployeeRepo extends JpaRepository<BiEmployee, Integer> {
	

}
