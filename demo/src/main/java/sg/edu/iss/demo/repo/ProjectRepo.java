package sg.edu.iss.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import sg.edu.iss.demo.model.Project;

public interface ProjectRepo extends JpaRepository<Project, Integer> {

	// Project without team members
	@Query("select count(p) from Project p where p.employees is empty")
	long countWithoutMembers();

	// Ordered List
	List<Project> findAllByOrderByProjectId();

}
