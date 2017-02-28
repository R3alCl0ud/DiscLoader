package io.discloader.discloader.network.voice;

import io.discloader.discloader.entity.voice.VoiceConnection;

import java.net.*;

//import com.neovisionaries.ws.client.WebSocket;
//import com.neovisionaries.ws.client.WebSocketAdapter;

public class UDPVoiceClient {

	public static final int IDENTIFY = 0;
	public static final int SELECT_PROTOCAL = 1;
	public static final int READY = 2;
	public static final int HEARTBEAT = 3;
	public static final int SESSION_DESCRIPTION = 4;
	public static final int SPEAKING = 5;

	@SuppressWarnings("unused")
	private DatagramSocket updSocket;
	
	@SuppressWarnings("unused")
	private final VoiceConnection connection;
	
	public UDPVoiceClient(VoiceConnection connection) {
		this.connection = connection;
	}

}
