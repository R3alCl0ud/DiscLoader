package io.discloader.discloader.common.exceptions;

/**
 * @author Perry Berman
 */
public class MissmatchException extends Exception {

	private static final long serialVersionUID = -3357290583469017656L;

	public MissmatchException() {
		super("Object Missmatch");
	}

	public MissmatchException(Object mis) {
		super(String.format("The type '%s' is the wrong type for this opperation ", mis.getClass().getName()));
	}

	public MissmatchException(String message) {
		super(message);
	}

	public MissmatchException(Throwable cause) {
		super(cause);
	}

	public MissmatchException(String message, Throwable cause) {
		super(message, cause);
	}

	public MissmatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
