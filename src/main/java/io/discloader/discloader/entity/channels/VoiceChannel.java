package io.discloader.discloader.entity.channels;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.Guild;
import io.discloader.discloader.entity.impl.IGuildChannel;
import io.discloader.discloader.entity.impl.IVoiceChannel;
import io.discloader.discloader.entity.voice.VoiceConnection;
import io.discloader.discloader.network.json.ChannelJSON;

/**
 * @author Perry Berman
 *
 */
public class VoiceChannel extends Channel implements IGuildChannel, IVoiceChannel {

	public int bitrate;

	public int userLimit;

	/**
	 * @param guild
	 * @param data
	 */
	public VoiceChannel(Guild guild, ChannelJSON data) {
		super(guild, data);

		this.type = "voice";
	}

	public void setup(ChannelJSON data) {
		super.setup(data);

		this.bitrate = data.bitrate;

		this.userLimit = data.user_limit;
	}

	@Override
	public CompletableFuture<VoiceConnection> join() {
		return null;
	}

	@Override
	public CompletableFuture<VoiceConnection> leave() {
		return null;
	}

	@Override
	public boolean isPrivate() {
		return false;
	}

	@Override
	public CompletableFuture<IGuildChannel> setName(String name) {
		return null;
	}

	@Override
	public CompletableFuture<IGuildChannel> setPosition(int position) {
		return null;
	}

	@Override
	public CompletableFuture<IGuildChannel> setPermissions(int allow, int deny, String type) {
		return null;
	}
}
