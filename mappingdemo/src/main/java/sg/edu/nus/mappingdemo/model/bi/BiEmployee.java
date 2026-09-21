package sg.edu.nus.mappingdemo.model.bi;

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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import sg.edu.nus.mappingdemo.model.Department;
import sg.edu.nus.mappingdemo.model.EmploymentType;

/**
 * Unidirectional Mapping - Employee is the owner for all associations
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class BiEmployee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int empId;	
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
	private BiCubicle biCubicle;
    // BiEmployee
    @OneToMany(mappedBy = "biEmployee", fetch = FetchType.LAZY)
    private List<BiGadget> uniGadgetsList;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<BiProject> uniProjectsList;
}
