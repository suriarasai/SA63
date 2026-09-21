package sg.edu.nus.mappingdemo.model.uni;

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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Unidirectional Mapping
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class UniEmployee {
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
	@JoinColumn(name = "cubicle_id", referencedColumnName = "cubicleId")
	private UniCubicle uniCubicle;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gadget_id", nullable = false)
	private List<UniGadget> uniGadgets;
    @JoinTable(
            name = "employee_project",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
        )
    private List<UniProject> uniProjects;
	
}
