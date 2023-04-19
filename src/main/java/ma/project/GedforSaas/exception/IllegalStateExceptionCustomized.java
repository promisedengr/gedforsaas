package ma.project.GedforSaas.exception;

public class IllegalStateExceptionCustomized extends IllegalArgumentException {

	public IllegalStateExceptionCustomized(String message) {
		super(message);
	}

	public IllegalStateExceptionCustomized(String message, Throwable throwable) {
		super(message, throwable);
	}

}
