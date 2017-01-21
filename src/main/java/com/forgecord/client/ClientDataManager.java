package main.java.com.forgecord.client;

import org.json.JSONObject;

import main.java.com.forgecord.structures.Channel;
import main.java.com.forgecord.structures.DMChannel;
import main.java.com.forgecord.structures.Guild;
import main.java.com.forgecord.structures.TextChannel;
import main.java.com.forgecord.structures.User;
import main.java.com.forgecord.structures.VoiceChannel;
import main.java.com.forgecord.util.constants;

public class ClientDataManager {

	public Client client;

	public ClientDataManager(Client client) {
		this.client = client;
	}

	public boolean pastReady() {
		return this.client.ws.status == constants.Status.READY;
	}

	public Guild newGuild(JSONObject data) {
		boolean already = this.client.guilds.containsKey(data.getString("id"));
		Guild guild = new Guild(this.client, data);
		this.client.guilds.put(guild.id, guild);
		if (this.pastReady() && !already) {
			this.client.emit(constants.Events.GUILD_CREATE, guild);
		}
		return guild;
	}

	public User newUser(JSONObject data) {
		if (this.client.users.containsKey(data.getString("id")))
			return this.client.users.get(data.getString("id"));
		User user = new User(this.client, data);
		this.client.users.put(user.id, user);
		return user;
	}

	public Channel newChannel(JSONObject data, Guild guild) {
		boolean already = this.client.channels.containsKey(data.getString("id"));
		Channel channel = null;
		if (data.getBoolean("isprivate")) {
			channel = new DMChannel(this.client, data);
		} else {
			if (data.getString("type") == "text") {
				channel = new TextChannel(this.client, data);
			} else if (data.getString("type") == "voice") {
				channel = new VoiceChannel(this.client, data);
			}
		}

		if (channel != null) {
			if (this.pastReady() && !already)
				this.client.emit(constants.Events.CHANNEL_CREATE, channel);
			this.client.channels.put(channel.id, channel);
			return channel;
		}
		return null;
	}
}
