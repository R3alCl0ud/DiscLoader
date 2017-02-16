package io.discloader.discloader.common.structures.channels;

import java.util.HashMap;

import io.discloader.discloader.common.structures.Guild;
import io.discloader.discloader.common.structures.GuildMember;
import io.discloader.discloader.network.gateway.json.ChannelJSON;

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
