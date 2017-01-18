package structures;

import org.json.JSONObject;

import client.Client;

public class Message {
	public Client client;
	
	public int id;
	public String content;
	
	public Message(Client client, JSONObject data) {
		this.client = client;
		
	}
}
