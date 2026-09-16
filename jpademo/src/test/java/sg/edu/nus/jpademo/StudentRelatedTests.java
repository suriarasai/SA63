package sg.edu.nus.jpademo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import sg.edu.nus.jpademo.model.Student;
import sg.edu.nus.jpademo.repo.StudentRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class StudentRelatedTests {
	
	@Autowired 
	private StudentRepository studentRepo;
	
	@Autowired 
	private TestEntityManager entityManager;
	
	@BeforeEach
	public void setUpData() {
		Student s1 = new Student("Mikhail", "SA63", LocalDate.of(2001, 12, 12),"Nerd", 4.5, 2026, 2027);
		Student s2 = new Student("Nan Wint", "SA63", LocalDate.of(2000, 1, 1),"Geek", 4.6, 2025, 2027);
		Student s3 = new Student("Alicia", "SA63", LocalDate.of(2001, 12, 12),"Ms.Familiar", 4.5, 2020, 2027);
		Student s4 = new Student("Quek", "SA63", LocalDate.of(2001, 12, 12),"Sounds Interesting", 5.0, 2000, 2027);
		Student s5 = new Student("Ramesh", "SA63", LocalDate.of(2001, 12, 12),"Mr.Polite", 4.5, 2019, 2027);
		Student s6 = new Student("Senthil", "SA63", LocalDate.of(2001, 12, 12),"Mr.Interactive", 4.5, 2007, 2027);
		//ArrayList<Student> slist = new ArrayList<Student>();
		//slist.add(s1);slist.add(s2);slist.add(s3);slist.add(s4);slist.add(s5);slist.add(s6);	
		entityManager.persist(s1);
		entityManager.persist(s2);
		entityManager.persist(s3);
		entityManager.persist(s4);
		entityManager.persist(s5);
		entityManager.persist(s6);
		entityManager.flush();
		entityManager.clear();
		
	}
	//@Nested
    //@DisplayName("Inherited CRUD methods - findAll, save, delete,findByID")
    
	/*
	 * class Crud {
	 * 
	 * @Test void findAll() { Student testStudent = new Student("Senthil", "SA63",
	 * LocalDate.of(2001, 12, 12),"Mr.Interactive", 4.5, 2026, 2027); List<Student>
	 * students = studentRepo.findAll(); //assertThat(students).hasSize(6);
	 * assertThat(students).contains(testStudent);
	 * //assertThat(students).extracting(Student::getName).contains("Senthil"); }
	 * 
	 * @Test void save() { Student testStudent = new Student("Tay Dominc", "SA63",
	 * LocalDate.of(2025, 12, 12),"Mr.Prodigy", 5.0, 2026, 2027);
	 * studentRepo.save(testStudent); List<Student> students =
	 * studentRepo.findAll(); assertThat(students).contains(testStudent); }
	 * 
	 * @Test void findById() { Optional<Student> sample = studentRepo.findById(1);
	 * System.out.println(sample.isEmpty()); assertThat(sample).isEmpty(); }
	 * 
	 * @Test void update() { // Option null, one , list get() top record
	 * Optional<Student> sample = studentRepo.findById(1);
	 * sample.get().setNickName("Mr.Nerd"); studentRepo.save(sample.get());
	 * assertThat(sample.get()).extracting(Student::getNickName).asString().contains
	 * ("Mr.Nerd"); }
	 * 
	 * @Test void delete() { Student testStudent = new Student("Senthil", "SA63",
	 * LocalDate.of(2001, 12, 12),"Mr.Interactive", 4.5, 2026, 2027);
	 * studentRepo.delete(testStudent);
	 * assertThat(studentRepo.findAll()).doesNotContain(testStudent); }
	 * 
	 * 
	 * }
	 */

	@Nested
	@DisplayName("Testing the finder quries") 
	class Queries {
		/**
		 * 	public List<Student> findStudentsByName(String name); 
		 * public List<Student> findStudentsByEnrollmentYearANDGraduationYear(Integer enrollmentYear, Integer graduationYear);
		 * public List<Student> queryStudentsByNickName(String nickName);
		 * public List<Student> getStudentsByEnrollmentYear(Integer enrollmentYear);
		 * public List<Student> readStudentsByDobLessThanEqualStudents(LocalDate date);
		 */
		
		@Test 
		void findStudentsByNameTest () {
			//List records
			System.out.println("*********************");
			List<Student> students = studentRepo.findAll();
			for (Student student : students) {
				System.out.println(student);
			}
			System.out.println("*********************");
			List<Student> result = studentRepo.findStudentsByName("Mikhail");
			for (Student student : result) {
				System.out.println(student.toString());
			}
			Student verify = new Student(7, "Mikhail", "SA63", LocalDate.of(2001, 12, 12),"Nerd", 4.5, 2026, 2027);
			assertThat(result).contains(verify);
		}
		
		@Test
		void getStudentsByEnrollmentYearTest() {
			
			List<Student> result = studentRepo.getStudentsByEnrollmentYear(2000);
			Student verify = new Student(4, "Quek", "SA63", LocalDate.of(2001, 12, 12),"Sounds Interesting", 5.0, 2000, 2027);
			assertThat(result).contains(verify);
		}
		
		
		
	}
	
	
	
	
	@AfterEach 
	public void teardownData() {
		
	}


}
