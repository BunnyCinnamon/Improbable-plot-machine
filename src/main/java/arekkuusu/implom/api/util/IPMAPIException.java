package arekkuusu.implom.api.util;

public class IPMAPIException extends RuntimeException {

	public IPMAPIException() {
	}

	public IPMAPIException(String message) {
		super("[IPM API] " + message);
	}

	public IPMAPIException(String message, Throwable cause) {
		super("[IPM API] " + message, cause);
	}

	public IPMAPIException(Throwable cause) {
		super(cause);
	}

	public IPMAPIException(String message, Throwable cause, boolean enableSuppression,
						   boolean writableStackTrace) {
		super("[IPM API] " + message, cause, enableSuppression, writableStackTrace);
	}
}
