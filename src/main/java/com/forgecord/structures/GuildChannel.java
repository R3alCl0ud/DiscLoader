package main.java.com.forgecord.structures;

import org.json.JSONObject;

import main.java.com.forgecord.client.Client;

public class GuildChannel extends TextChannel {
	public GuildChannel(Client client, JSONObject data) {
		super(client, data);
	}
}
