package sg.edu.iss.demo.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer empId;
	private String name;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private LocalDate doj;
	private Double pay;
	private String title;
	@Enumerated(EnumType.ORDINAL)
	private EmploymentType empType;
	@Enumerated(EnumType.STRING)
	private Department department;
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Cubicle cubicle;
	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
	private List<Gadget> gadgets;
	@ManyToMany(fetch = FetchType.LAZY)
	private List<Project> projects;
}
