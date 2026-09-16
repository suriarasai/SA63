package sg.edu.nus.jpademo.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import sg.edu.nus.jpademo.model.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

	@Query("SELECT s FROM Student s WHERE s.name = :name")
	public List<Student> findStudentsByName(String name);
	
	public List<Student> findStudentsByEnrollmentYearAndGraduationYear(Integer enrollmentYear, Integer graduationYear);
	
	public List<Student> queryStudentsByNickName(String nickName);
	
	public List<Student> getStudentsByEnrollmentYear(Integer enrollmentYear);
	
	//public List<Student> readStudentsByDobLessThanEqual(LocalDate date);
	
	

}
