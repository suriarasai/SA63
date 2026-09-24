package sg.edu.iss.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Gadget {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer gadgetId;

	@NotBlank(message = "Name is required")
	private String name;
	private String description;

	@ManyToOne
	@JoinColumn(name = "emp_id")
	private Employee employee;

}
