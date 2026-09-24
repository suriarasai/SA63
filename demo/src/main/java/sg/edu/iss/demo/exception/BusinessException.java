package sg.edu.iss.demo.exception;

/** When a business rule was broken (e.g. cubicle already taken). */
public class BusinessException extends RuntimeException {
	public BusinessException(String message) {
		super(message);
	}
}
