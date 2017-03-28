package io.discloader.discloader.common.exceptions;

public class PermissionsException extends Exception {

	private static final long serialVersionUID = -7868362520929649963L;

	public PermissionsException() {
		super("Insuficient Permissions");
	}

	public PermissionsException(String message) {
		super(message);
	}

	public PermissionsException(Throwable cause) {
		super(cause);
	}

	public PermissionsException(String message, Throwable cause) {
		super(message, cause);
	}

	public PermissionsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
