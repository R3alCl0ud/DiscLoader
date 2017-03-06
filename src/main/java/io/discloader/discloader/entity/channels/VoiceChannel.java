package io.discloader.discloader.entity.channels;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.Guild;
import io.discloader.discloader.entity.impl.IGuildChannel;
import io.discloader.discloader.entity.impl.IVoiceChannel;
import io.discloader.discloader.entity.voice.VoiceConnection;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.util.DLUtil.ChannelType;

/**
 * @author Perry Berman
 *
 */
public class VoiceChannel extends GuildChannel implements IGuildChannel, IVoiceChannel {

	/**
	 * The channel's bitrate
	 */
	public int bitrate;

	/**
	 * The limit of user that can be in this channel at one time
	 */
	public int userLimit;

	/**
	 * @param guild The guild the channel is in
	 * @param data The channel's data
	 */
	public VoiceChannel(Guild guild, ChannelJSON data) {
		super(guild, data);

		this.type = ChannelType.VOICE;

		this.name = data.name;
	}

	@Override
	public CompletableFuture<VoiceConnection> join() {
		CompletableFuture<VoiceConnection> future = new CompletableFuture<VoiceConnection>();
		VoiceConnection connection = new VoiceConnection(this, future);
		this.loader.voiceConnections.put(this.guild.id, connection);
		return future;
	}

	@Override
	public CompletableFuture<VoiceConnection> leave() {
		return null;
	}

	@Override
	public CompletableFuture<VoiceChannel> setName(String name) {
		return null;
	}

	@Override
	public CompletableFuture<VoiceChannel> setPermissions(int allow, int deny, String type) {
		return null;
	}

	@Override
	public CompletableFuture<VoiceChannel> setPosition(int position) {
		return null;
	}

	public void setup(ChannelJSON data) {
		super.setup(data);

		this.bitrate = data.bitrate;

		this.userLimit = data.user_limit;
	}
}
