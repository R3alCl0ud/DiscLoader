package main.java.com.forgecord.structures;

import java.util.Date;
import java.util.HashMap;

import org.json.JSONObject;

import main.java.com.forgecord.client.Client;

public class Guild {
	
	public Client client;

	public final String id;
	public String name;
	public String icon;
	public String splash;
	public String ownerID;
	public String region;
	public String afkChannelID;
	
	public int afkTimeout;
	public int verificationLevel;
	public int mfaLevel;
	public int memberCount;
	
	public boolean embedEnabled;
	public boolean large;
	public boolean unavailable;
	
	public Date joinedAt;
	
	public HashMap<String, GuildChannel> channels;
	public HashMap<String, GuildMember> members;
	public HashMap<String, Role> roles;
	public HashMap<String, Emoji> emojis;
	
	public Guild(Client client, JSONObject data) {
		this.client = client;
		this.id = data.getString("id");
		
		this.channels = new HashMap<String, GuildChannel>();
		this.members = new HashMap<String, GuildMember>();
	} 
	
	public void setup(JSONObject data) {
		this.name = data.getString("name");
		this.icon = data.getString("icon");
		this.splash = data.getString("splash");
		this.ownerID = data.getString("owner_id");
		
		if (data.has("members")) {
			this.members.clear();
			data.getJSONArray("members").forEach(guildMember -> this.addMember(guildMember, false));
		}
		
	}

	public GuildMember addMember(Object guildMember, boolean emitEvent) {
		// TODO Auto-generated method stub
		return null;
	}
}
