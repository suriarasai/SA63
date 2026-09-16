package sg.edu.nus.jpademo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.jpademo.model.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
	

}
