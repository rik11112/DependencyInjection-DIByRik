package DIByRik.exceptions;

public class InputMappingException extends RuntimeException {
	public InputMappingException() {
		super("Route cannot contain spaces");
	}
}
