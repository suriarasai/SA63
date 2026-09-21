package sg.edu.nus.mappingdemo.model.bi;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
public class BiGadget {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer gadgetId;
	private String name;
	private String description;
	@ManyToOne
	private BiEmployee biemployee;

}
