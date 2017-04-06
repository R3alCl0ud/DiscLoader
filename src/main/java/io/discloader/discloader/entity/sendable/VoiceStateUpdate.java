package io.discloader.discloader.entity.sendable;

import io.discloader.discloader.entity.channel.IVoiceChannel;
import io.discloader.discloader.entity.guild.IGuild;

/**
 * @author Perry Berman
 */
public class VoiceStateUpdate {

	public String guild_id;
	public String channel_id;
	// public String session_id;

	public boolean self_mute;
	public boolean self_deaf;

	public VoiceStateUpdate(IGuild guild, IVoiceChannel channel, boolean mute, boolean deaf) {
		this.guild_id = guild.getID();
		if (channel != null) channel_id = channel.getID();
		self_mute = mute;
		self_deaf = deaf;
	}

}
