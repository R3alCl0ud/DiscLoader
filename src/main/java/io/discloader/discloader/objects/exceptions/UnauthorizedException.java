package io.discloader.discloader.objects.exceptions;

/**
 * @author Perry Berman
 *
 */
public class UnauthorizedException extends Exception {

	private static final long serialVersionUID = 4020942557428460581L;

	public UnauthorizedException() {
		
	}

	/**
	 * @param message
	 */
	public UnauthorizedException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public UnauthorizedException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public UnauthorizedException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public UnauthorizedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
