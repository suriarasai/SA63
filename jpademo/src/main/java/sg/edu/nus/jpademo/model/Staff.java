package sg.edu.nus.jpademo.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="staff")
public class Staff {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer staffId;
	@Column(length = 100)
	private String name;
	@Column(nullable = false)
	private LocalDate dob;
	private String nickName;
	private Double pay;
	private Integer joiningYear;

}
