package main.java.com.forgecord.structures;

import org.json.JSONObject;

import main.java.com.forgecord.client.Client;

public class VoiceChannel extends Channel {

	public VoiceChannel(Client client, JSONObject data) {
		super(client, data);
	}

}
