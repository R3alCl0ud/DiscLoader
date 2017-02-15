package io.discloader.discloader.objects.structures;

import java.util.HashMap;

import io.discloader.discloader.objects.gateway.ChannelJSON;

/**
 * @author Perry Berman
 *
 */
public class VoiceChannel extends Channel {

	public final HashMap<String, GuildMember> members;

	public int bitrate;

	public int userLimit;

	/**
	 * @param guild
	 * @param data
	 */
	public VoiceChannel(Guild guild, ChannelJSON data) {
		super(guild, data);

		this.members = new HashMap<String, GuildMember>();

		this.type = "voice";
	}

	public void setup(ChannelJSON data) {
		super.setup(data);

		this.bitrate = data.bitrate;

		this.userLimit = data.user_limit;
	}
}
