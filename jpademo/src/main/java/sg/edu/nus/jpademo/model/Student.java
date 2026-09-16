package sg.edu.nus.jpademo.model;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="student")
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer matricId;
	@Column(length = 100)
	private String name;
	@Column(length = 12, nullable = false)
	private String classEnrolled;
	@Column(nullable = false)
	private LocalDate dob;
	private String nickName;
	private Double cgpa;
	private Integer enrollmentYear;
	private Integer graduationYear;
	public Student(String name, String classEnrolled, LocalDate dob, String nickName, Double cgpa,
			Integer enrollmentYear, Integer graduationYear) {
		super();
		this.name = name;
		this.classEnrolled = classEnrolled;
		this.dob = dob;
		this.nickName = nickName;
		this.cgpa = cgpa;
		this.enrollmentYear = enrollmentYear;
		this.graduationYear = graduationYear;
	}
	public Student() {
		super();
	}
	public Integer getMatricId() {
		return matricId;
	}
	public void setMatricId(Integer matricId) {
		this.matricId = matricId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClassEnrolled() {
		return classEnrolled;
	}
	public void setClassEnrolled(String classEnrolled) {
		this.classEnrolled = classEnrolled;
	}
	public LocalDate getDob() {
		return dob;
	}
	public void setDob(LocalDate dob) {
		this.dob = dob;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Double getCgpa() {
		return cgpa;
	}
	public void setCgpa(Double cgpa) {
		this.cgpa = cgpa;
	}
	public Integer getEnrollmentYear() {
		return enrollmentYear;
	}
	public void setEnrollmentYear(Integer enrollmentYear) {
		this.enrollmentYear = enrollmentYear;
	}
	public Integer getGraduationYear() {
		return graduationYear;
	}
	public void setGraduationYear(Integer graduationYear) {
		this.graduationYear = graduationYear;
	}
	@Override
	public String toString() {
		return "Student [matricId=" + matricId + ", name=" + name + ", classEnrolled=" + classEnrolled + ", dob=" + dob
				+ ", nickName=" + nickName + ", cgpa=" + cgpa + ", enrollmentYear=" + enrollmentYear
				+ ", graduationYear=" + graduationYear + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(matricId);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		return Objects.equals(matricId, other.matricId);
	}
	

}
