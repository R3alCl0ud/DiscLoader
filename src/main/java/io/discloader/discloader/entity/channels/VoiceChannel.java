package io.discloader.discloader.entity.channels;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.Guild;
import io.discloader.discloader.entity.impl.IGuildChannel;
import io.discloader.discloader.entity.impl.IVoiceChannel;
import io.discloader.discloader.entity.voice.VoiceConnection;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.util.DLUtil.ChannelType;

/**
 * Represents a voice channel in a guild
 * 
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

	/**
	 * Changes the channels settings
	 * 
	 * @param name The new name for the channel
	 * @param position The new position for the channel
	 * @return A Future that completes with a voice channel if successful
	 */
	public CompletableFuture<VoiceChannel> edit(String name, int position) {
		return edit(name, position, bitrate, userLimit);
	}

	/**
	 * Changes the channels settings
	 * 
	 * @param name The new name for the channel
	 * @param position The new position for the channel
	 * @param bitrate The new {@link #bitrate}
	 * @param userLimit The new {@link #userLimit}
	 * @return A Future that completes with a voice channel if successful
	 */
	public CompletableFuture<VoiceChannel> edit(String name, int position, int bitrate, int userLimit) {
		CompletableFuture<VoiceChannel> future = new CompletableFuture<>();
		loader.rest.modifyGuildChannel(this, name, null, position, bitrate, userLimit).thenAcceptAsync(channel -> {
			future.complete((VoiceChannel) channel);
		});
		return future;
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

	public CompletableFuture<VoiceChannel> setBitrate(int bitrate) {
		return edit(name, position, bitrate, userLimit);
	}

	@Override
	public CompletableFuture<VoiceChannel> setName(String name) {
		return edit(name, position, bitrate, userLimit);
	}

	@Override
	public CompletableFuture<VoiceChannel> setPermissions(int allow, int deny, String type) {
		CompletableFuture<VoiceChannel> future = new CompletableFuture<>();
		super.setPermissions(allow, deny, type).thenAcceptAsync(channel -> {
			future.complete((VoiceChannel) channel);
		});
		return future;
	}

	@Override
	public CompletableFuture<VoiceChannel> setPosition(int position) {
		return edit(name, position, bitrate, userLimit);
	}

	public void setup(ChannelJSON data) {
		super.setup(data);

		this.bitrate = data.bitrate;

		this.userLimit = data.user_limit;
	}

	public CompletableFuture<VoiceChannel> setUserLimit(int userLimit) {
		return edit(name, position, bitrate, userLimit);
	}
}
