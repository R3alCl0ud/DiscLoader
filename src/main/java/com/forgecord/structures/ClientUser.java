package main.java.com.forgecord.structures;

import org.json.JSONObject;

import main.java.com.forgecord.client.Client;

public class ClientUser extends User {
	public JSONObject settings;

	public ClientUser(Client client, JSONObject data) {
		super(client, data);
	}

}
