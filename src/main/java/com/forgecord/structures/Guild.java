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
	
	/**
	 * are embeds enabled
	 */
	public boolean embedEnabled;
	
	/**
	 * is the guild large (more than 250 members)
	 */
	public boolean large;
	
	/**
	 * is the guild unavailable
	 */
	public boolean unavailable;
	
	/**
	 * The {@link Date} at which the {@link Client} joined the guild.
	 */
	public Date joinedAt;
	
	/**
	 * The guild's channels stored as {@link HashMap}<{@link String Snowflake}, {@link GuildChannel}>
	 * @author perryberman
	 */
	public HashMap<String, GuildChannel> channels;
	
	/**
	 * The guild's members stored as {@link HashMap}<{@link String Snowflake}, {@link GuildMember}>
	 * @author Perry Berman
	 */
	public HashMap<String, GuildMember> members;
	
	/**
	 * The guild's roles stored as {@link HashMap}<{@link String Snowflake}, {@link Role}>
	 * @author perryberman
	 */
	public HashMap<String, Role> roles;
	
	/**
	 * The guild's emojis stored as {@link HashMap}<{@link String Snowflake}, {@link Emoji}>
	 * @author perryberman
	 */
	public HashMap<String, Emoji> emojis;
	
	/**
	 * @param client The {@link Client} that cached the guild
	 * @param data The guilds data
	 */
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

	/**
	 * @param guildMember
	 * @param emitEvent 
	 * @return {@link GuildMember GuildMember}
	 */
	public GuildMember addMember(Object guildMember, boolean emitEvent) {
		return null;
	}
}
