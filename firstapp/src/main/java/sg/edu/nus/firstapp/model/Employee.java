package sg.edu.nus.firstapp.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Employee {
	// Here Spring MVC create the id
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int empId;
	
	private String name;
	
	private LocalDate doj;
	
	private Double pay;
	
	private String title;
	
	private EmploymentType empType;
	
	private Department department;

	public Employee(int empId, String name, LocalDate doj, Double pay, String title, EmploymentType empType,
			Department department) {
		super();
		this.empId = empId;
		this.name = name;
		this.doj = doj;
		this.pay = pay;
		this.title = title;
		this.empType = empType;
		this.department = department;
	}

	public Employee(String name, LocalDate doj, Double pay, String title, EmploymentType empType,
			Department department) {
		super();
		this.name = name;
		this.doj = doj;
		this.pay = pay;
		this.title = title;
		this.empType = empType;
		this.department = department;
	}

	public Employee() {
		super();
	}

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDoj() {
		return doj;
	}

	public void setDoj(LocalDate doj) {
		this.doj = doj;
	}

	public Double getPay() {
		return pay;
	}

	public void setPay(Double pay) {
		this.pay = pay;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public EmploymentType getEmpType() {
		return empType;
	}

	public void setEmpType(EmploymentType empType) {
		this.empType = empType;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@Override
	public String toString() {
		return "Employee [empId=" + empId + ", name=" + name + ", doj=" + doj + ", pay=" + pay + ", title=" + title
				+ ", empType=" + empType + ", department=" + department + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(Integer.valueOf(empId));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		return empId == other.empId;
	}
	
	
	

}
