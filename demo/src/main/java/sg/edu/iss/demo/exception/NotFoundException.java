package sg.edu.iss.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
/** Any Run Time Exception **/
public class NotFoundException extends RuntimeException {
	public NotFoundException(String message) {
		super(message);
	}
}
