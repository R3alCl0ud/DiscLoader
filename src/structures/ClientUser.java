package structures;

import org.json.JSONObject;

import client.Client;

public class ClientUser extends User {
	public ClientUser(Client client, JSONObject data) {
		super(client, data);
	}

}
