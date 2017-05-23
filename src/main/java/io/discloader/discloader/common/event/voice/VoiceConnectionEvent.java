package io.discloader.discloader.common.event.voice;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.voice.VoiceConnection;

public class VoiceConnectionEvent extends DLEvent {

	private VoiceConnection connection;

	public VoiceConnectionEvent(VoiceConnection connection) {
		super(connection.getLoader());
		this.connection = connection;
	}

	/**
	 * @return the connection
	 */
	public VoiceConnection getConnection() {
		return connection;
	}
}
