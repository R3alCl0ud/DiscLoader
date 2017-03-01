package io.discloader.discloader.common.exceptions;

public class InsucficientPermissionsException extends Exception {

	private static final long serialVersionUID = -7868362520929649963L;

	public InsucficientPermissionsException() {
	}

	public InsucficientPermissionsException(String message) {
		super(message);
	}

	public InsucficientPermissionsException(Throwable cause) {
		super(cause);
	}

	public InsucficientPermissionsException(String message, Throwable cause) {
		super(message, cause);
	}

	public InsucficientPermissionsException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
