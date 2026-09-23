package sg.edu.nus.firstapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Step 2 - Implement ConstraintValidator<Annotation, FieldType>.
 */
public class PhoneNumberValidator implements ConstraintValidator<ValidPhone, String> {

	private String regexp;

	/**
	 * initialize() receives the annotation instance, so we can read its attributes.
	 */
	@Override
	public void initialize(ValidPhone vp) {
		this.regexp = vp.regexp();
	}

	/** isValid() holds the core logic: true = passes, false = fails. */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext ctx) {
		if (value == null) {
			return true; // @NotNull / @NotBlank is responsible for null
		}
		return value.matches(regexp);
	}
}
