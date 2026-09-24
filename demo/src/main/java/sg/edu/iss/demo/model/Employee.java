package sg.edu.iss.demo.model;

import java.time.LocalDate;
import java.util.ArrayList;
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
import jakarta.persistence.JoinTable;
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
	// Owner. 
	//No cascade, so deleting an employee does not delete the cubicle.
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cubicle_id", unique = true)
	private Cubicle cubicle;

	// Inverse side of Gadget
	@OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
	private List<Gadget> gadgets = new ArrayList<>();

	// Owner.
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "employee_project",
			joinColumns = @JoinColumn(name = "emp_id"),
			inverseJoinColumns = @JoinColumn(name = "project_id"))
	private List<Project> projects = new ArrayList<>();

	// ---------- helpers that keep both sides in sync ----------

	public void assignCubicle(Cubicle newCubicle) {
		if (this.cubicle != null) {
			this.cubicle.setEmployee(null);
		}
		this.cubicle = newCubicle;
		if (newCubicle != null) {
			newCubicle.setEmployee(this);
		}
	}

	public void addGadget(Gadget gadget) {
		Employee previous = gadget.getEmployee();
		if (previous != null && previous != this) {
			previous.getGadgets().remove(gadget);
		}
		gadget.setEmployee(this);
		if (!gadgets.contains(gadget)) {
			gadgets.add(gadget);
		}
	}

	public void removeGadget(Gadget gadget) {
		gadgets.remove(gadget);
		gadget.setEmployee(null);
	}

	public void joinProject(Project project) {
		if (!projects.contains(project)) {
			projects.add(project);
			project.getEmployees().add(this);
		}
	}

	public void leaveProject(Project project) {
		projects.remove(project);
		project.getEmployees().remove(this);
	}
}
