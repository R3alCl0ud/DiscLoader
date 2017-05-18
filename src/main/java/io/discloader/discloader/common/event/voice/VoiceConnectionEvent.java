package io.discloader.discloader.common.event.voice;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.voice.VoiceConnect;

public class VoiceConnectionEvent extends DLEvent {

	private VoiceConnect connection;

	public VoiceConnectionEvent(VoiceConnect connection) {
		super(connection.getLoader());
		this.connection = connection;
	}

	/**
	 * @return the connection
	 */
	public VoiceConnect getConnection() {
		return connection;
	}
}
