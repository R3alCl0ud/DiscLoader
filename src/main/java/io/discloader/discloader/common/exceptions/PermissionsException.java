package io.discloader.discloader.common.exceptions;

import io.discloader.discloader.network.json.ExceptionJSON;

public class PermissionsException extends RuntimeException {
	
	private static final long serialVersionUID = -7868362520929649963L;
	private int code;
	
	
	public PermissionsException() {
		super("Insuficient Permissions");
	}
	public PermissionsException(ExceptionJSON data) {
		super(data.message);
		code = data.code;
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
	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}
	
}
