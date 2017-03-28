package io.discloader.discloader.common.exceptions;

/**
 * @author Perry Berman
 */
public class VoiceConnectionException extends Exception {

	private static final long serialVersionUID = -8083311734809159902L;

	public VoiceConnectionException() {
		super("Error in voice connection");
	}

	public VoiceConnectionException(String message) {
		super(message);
	}

}
