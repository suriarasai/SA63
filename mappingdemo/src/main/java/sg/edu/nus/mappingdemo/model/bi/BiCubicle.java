package sg.edu.nus.mappingdemo.model.bi;

import org.springframework.cglib.core.GeneratorStrategy;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class BiCubicle {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer cubicleId;
	private String name;
	private String location;
	private String description;
	// BiCubicle
	@OneToOne(mappedBy = "biCubicle")
	private BiEmployee biEmployee;


}
