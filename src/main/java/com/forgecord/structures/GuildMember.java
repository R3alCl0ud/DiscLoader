package main.java.com.forgecord.structures;

import java.util.Date;
import java.util.HashMap;

import org.json.JSONObject;

import main.java.com.forgecord.client.Client;

public class GuildMember {
	
	public Client client;
	
	public User user;
	
	public String nickname;
	
	public HashMap<String, Role> roles;
	
	public Date joinedAt;
	
	public boolean deaf;
	public boolean mute;
	
	public GuildMember(Client client, JSONObject data) {
		this.client = client;
		
		this.user = new User(this.client, data.getJSONObject("user"));
	}
}
