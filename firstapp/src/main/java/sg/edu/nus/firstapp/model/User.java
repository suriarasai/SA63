package sg.edu.nus.firstapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import sg.edu.nus.firstapp.validation.ValidPhone;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class User {
 
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	 
	    @NotBlank(message = "Name is required")
	    @Size(min=3,max = 15, message = "Name must be at least 3 characters and at most 15 characters")
	    private String name;

	    @NotBlank(message = "Email is required")
	    @Email(message = "Email format is invalid")
	    private String email;
	 
	    @NotBlank(message = "Phone number is required")
        @ValidPhone(
        regexp = "^\\+65?[987][0-9]{7}$",
        message = "Phone must be optional +65 followed by 8 digits"
        )
	    private String phoneNumber;
	    
	    

}
