package main.java.com.forgecord.structures;

import org.json.JSONObject;

import main.java.com.forgecord.client.Client;

public class Message {
	public Client client;
	
	public Channel channel;
	
	public Guild guild;
	
	public User author;
	public User[] mentions;
	public Role[] mentionedRoles;
	
	public String id;
	public String content;
	public String nonce;
	
	public boolean tts;
	public boolean everyone;
	public boolean pinned;
	
	public Message(Client client, JSONObject data) {
		this.client = client;
		
		this.id = data.getString("id");
		
		this.channel = client.channels.get(data.getString("channel_id"));
	}
}
