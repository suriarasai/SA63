package sg.edu.iss.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer projectId;

	@NotBlank(message = "Name is required")
	private String name;

	@Enumerated(EnumType.STRING)
	private Department department;

	@PositiveOrZero(message = "Budget cannot be negative")
	private Double budget;

	@ManyToMany(mappedBy = "projects")
	private List<Employee> employees = new ArrayList<>();

}
