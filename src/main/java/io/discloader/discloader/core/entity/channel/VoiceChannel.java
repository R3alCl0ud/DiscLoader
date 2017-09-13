package io.discloader.discloader.core.entity.channel;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.channel.IGuildVoiceChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.voice.VoiceConnection;
import io.discloader.discloader.network.json.ChannelJSON;

/**
 * Represents a voice channel in a guild
 * 
 * @author Perry Berman
 */
public class VoiceChannel extends GuildChannel implements IGuildVoiceChannel {

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
	public VoiceChannel(IGuild guild, ChannelJSON data) {
		super(guild, data);

		// this.type = ChannelType.VOICE;

		this.name = data.name;
	}

	/**
	 * Changes the channels settings
	 * 
	 * @param name The new name for the channel
	 * @param position The new position for the channel
	 * @return A Future that completes with a voice channel if successful
	 */
	public CompletableFuture<IGuildChannel> edit(String name, int position) {
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
	public CompletableFuture<IGuildChannel> edit(String name, int position, int bitrate, int userLimit) {
		CompletableFuture<IGuildChannel> future = new CompletableFuture<>();
		loader.rest.modifyGuildChannel(this, name, null, position, bitrate, userLimit).thenAcceptAsync(channel -> {
			future.complete(channel);
		});
		return future;
	}

	@Override
	public CompletableFuture<VoiceConnection> join() {
		if (EntityRegistry.getVoiceConnectionByID(guild.getID()) != null) {
			VoiceConnection connection = EntityRegistry.getVoiceConnectionByID(guild.getID());
			connection.updateChannel(this);
			return CompletableFuture.completedFuture(connection);
		}
		CompletableFuture<VoiceConnection> future = new CompletableFuture<VoiceConnection>();
		VoiceConnection connection = new VoiceConnection(this, future);
		EntityRegistry.putVoiceConnection(connection);
		return future;
	}

	@Override
	public CompletableFuture<VoiceConnection> leave() {
		if (EntityRegistry.getVoiceConnectionByID(guild.getID()) != null) {
			return EntityRegistry.getVoiceConnectionByID(guild.getID()).disconnect();
		}
		return null;
	}

	public CompletableFuture<IGuildChannel> setBitrate(int bitrate) {
		return edit(name, position, bitrate, userLimit);
	}

	@Override
	public CompletableFuture<IGuildChannel> setName(String name) {
		return edit(name, position, bitrate, userLimit);
	}

	@Override
	public CompletableFuture<IGuildChannel> setPosition(int position) {
		return edit(name, position, bitrate, userLimit);
	}

	@Override
	public void setup(ChannelJSON data) {
		super.setup(data);

		this.bitrate = data.bitrate;

		this.userLimit = data.user_limit;
	}

	public CompletableFuture<IGuildChannel> setUserLimit(int userLimit) {
		return edit(name, position, bitrate, userLimit);
	}
}
