package io.discloader.discloader.common.exceptions;

/**
 * Thrown when a REST endpoint is called when the client doesn't have sufficient permissions to access said endpoint
 * 
 * @author Perry Berman
 *
 */
public class UnauthorizedException extends RuntimeException {
	
	private static final long serialVersionUID = 4020942557428460581L;
	
	/**
	 * Creates a new UnauthorizedException
	 * 
	 * @param message The error message
	 */
	public UnauthorizedException(String message) {
		super(message);
	}
}
