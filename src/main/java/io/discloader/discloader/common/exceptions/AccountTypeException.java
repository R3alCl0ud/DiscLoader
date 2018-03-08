package io.discloader.discloader.common.exceptions;

import io.discloader.discloader.network.json.ExceptionJSON;

/**
 * Thrown
 * 
 * 
 * @author Perry Berman
 *
 */
public class AccountTypeException extends RuntimeException {

	private static final long serialVersionUID = -834830576849056357L;

	public AccountTypeException(ExceptionJSON data) {
		super(data.message);
	}

	/**
	 * Constructs a new runtime exception with the specified detail message. The
	 * cause is not initialized, and may subsequently be initialized by a call to
	 * {@link #initCause}.
	 *
	 * @param message
	 *            the detail message. The detail message is saved for later
	 *            retrieval by the {@link #getMessage()} method.
	 */
	public AccountTypeException(String message) {
		super(message);
	}

	/**
	 * Constructs a new runtime exception with the specified cause and a detail
	 * message of <tt>(cause==null ? null : cause.toString())</tt> (which typically
	 * contains the class and detail message of <tt>cause</tt>). This constructor is
	 * useful for runtime exceptions that are little more than wrappers for other
	 * throwables.
	 *
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method). (A <tt>null</tt> value is permitted,
	 *            and indicates that the cause is nonexistent or unknown.)
	 * @since 1.4
	 *
	 */
	public AccountTypeException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AccountTypeException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public AccountTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
