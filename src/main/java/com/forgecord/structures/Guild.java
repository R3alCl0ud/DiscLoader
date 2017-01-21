package main.java.com.forgecord.structures;

import java.util.Date;
import java.util.HashMap;

import org.json.JSONObject;

import main.java.com.forgecord.client.Client;
import main.java.com.forgecord.util.constants;

public class Guild {

	public Client client;

	public String id;
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
	 * The guild's channels stored as {@link HashMap}<{@link String Snowflake},
	 * {@link GuildChannel}>
	 * 
	 * @author Perry Berman
	 */
	public HashMap<String, GuildChannel> channels;

	/**
	 * The guild's members stored as {@link HashMap}<{@link String Snowflake},
	 * {@link GuildMember}>
	 * 
	 * @author Perry Berman
	 */
	public HashMap<String, GuildMember> members;

	/**
	 * The guild's roles stored as {@link HashMap}<{@link String Snowflake},
	 * {@link Role}>
	 * 
	 * @author Perry Berman
	 */
	public HashMap<String, Role> roles;

	/**
	 * The guild's emojis stored as {@link HashMap}<{@link String Snowflake},
	 * {@link Emoji}>
	 * 
	 * @author Perry Berman
	 */
	public HashMap<String, Emoji> emojis;

	/**
	 * @param client
	 *            The {@link Client} that cached the guild
	 * @param data
	 *            The guilds data
	 */
	public Guild(Client client, JSONObject data) {
		this.client = client;

		this.channels = new HashMap<String, GuildChannel>();
		this.members = new HashMap<String, GuildMember>();
		this.roles = new HashMap<String, Role>();
		this.emojis = new HashMap<String, Emoji>();
		if (data.getBoolean("unavailable")) {
			this.unavailable = true;

			this.id = data.getString("id");
		} else {
			this.unavailable = false;
			this.setup(data);
		}
	}

	public void setup(JSONObject data) {
		this.name = data.getString("name");
		this.icon = data.getString("icon");
		System.out.println("test");
		this.splash = data.has("splash") ? data.getString("splash") : null;
		System.out.println("test1");
		this.ownerID = data.has("owner_id") ? data.getString("owner_id") : null;
		System.out.println("Test2");
		if (data.has("members")) {
			this.members.clear();
			for (Object guildUser : data.getJSONArray("members").toList()) {
				System.out.println(((JSONObject) guildUser).getString("nick"));
				this.addMember((JSONObject) guildUser, false);
			}
		}

		this.unavailable = data.getBoolean("unavailable");
		System.out.println("fuck it");
	}

	/**
	 * @param guildMember
	 * @param emitEvent
	 * @return {@link GuildMember GuildMember}
	 */
	public GuildMember addMember(JSONObject guildMember, boolean emitEvent) {
		boolean existing = this.members.containsKey(guildMember.getJSONObject("user").getString("id"));

		GuildMember member = new GuildMember(this, guildMember);

		this.members.put(member.user.id, member);
		if (this.client.ws.status == constants.Status.READY && emitEvent && !existing) {
			this.client.emit(constants.Events.GUILD_MEMBER_ADD, member);
		}

		return member;
	}
}
