package sg.edu.nus.mappingdemo.model.uni;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class UniGadget {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer gadgetId;
	private String name;
	private String description;

}
