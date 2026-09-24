package sg.edu.iss.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Cubicle {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cubicleId;

	@NotBlank(message = "Name is required")
	private String name;
	private String location;
	private String description;

	@OneToOne(mappedBy = "cubicle")
	private Employee employee;
	//Helper Method
	public boolean isFree() {
		return employee == null;
	}
}
