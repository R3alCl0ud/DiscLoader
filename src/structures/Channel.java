package structures;

import client.Client;
import org.json.JSONObject;
import org.json.JSONException;

public class Channel {
	
	public int id;
	public Client client;
	
	public Channel(Client client) {
		this.client = client;
	}
	
	public void sendMessage(String message) {
//		this.client.rest;
	}
}
