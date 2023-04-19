package ma.project.GedforSaas.exception;

public class AuthenticationFailedException extends RuntimeException {

	public AuthenticationFailedException(String message) {
		super(message);
	}

	public AuthenticationFailedException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AuthenticationFailedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public AuthenticationFailedException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public AuthenticationFailedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
