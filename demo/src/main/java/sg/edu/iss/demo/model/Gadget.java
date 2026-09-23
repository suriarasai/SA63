package sg.edu.iss.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Gadget {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer gadgetId;
	private String name;
	private String description;
	@ManyToOne
	private Employee employee;

}
