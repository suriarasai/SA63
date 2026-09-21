package sg.edu.nus.mappingdemo.repo.uni;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.mappingdemo.model.uni.UniEmployee;

public interface UniEmployeeRepo extends JpaRepository<UniEmployee, Integer> {

}
