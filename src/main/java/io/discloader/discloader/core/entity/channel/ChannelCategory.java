package io.discloader.discloader.core.entity.channel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;

import io.discloader.discloader.common.registry.EntityBuilder;
import io.discloader.discloader.entity.IOverwrite;
import io.discloader.discloader.entity.channel.ChannelTypes;
import io.discloader.discloader.entity.channel.IChannelCategory;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.channel.IGuildTextChannel;
import io.discloader.discloader.entity.channel.IGuildVoiceChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.rest.RESTOptions;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

public class ChannelCategory extends GuildChannel implements IChannelCategory {

	public ChannelCategory(IGuild guild, ChannelJSON channel) {
		super(guild, channel);
	}

	@Override
	public <T extends IGuildChannel> CompletableFuture<T> addChannel(T channel) {
		CompletableFuture<T> future = new CompletableFuture<>();
		JSONObject data = new JSONObject().put("parent_id", SnowflakeUtil.asString(this));
		loader.rest.request(Methods.PATCH, Endpoints.channel(channel.getID()), new RESTOptions(data), ChannelJSON.class).thenAcceptAsync(d -> {
			@SuppressWarnings("unchecked")
			T newChannel = (T) EntityBuilder.getChannelFactory().buildChannel(d, guild, false);
			future.complete(newChannel);
		}).exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	@Override
	public CompletableFuture<IGuildChannel> createChannel(String name, ChannelTypes type) {
		return null;
	}

	@Override
	public CompletableFuture<IGuildChannel> createChannel(String name, ChannelTypes type, IOverwrite... overwrites) {
		return null;
	}

	@Override
	public CompletableFuture<IGuildTextChannel> createTextChannel(String name) {
		return null;
	}

	@Override
	public CompletableFuture<IGuildTextChannel> createTextChannel(String name, IOverwrite... overwrites) {
		return null;
	}

	@Override
	public CompletableFuture<IGuildVoiceChannel> createVoiceChannel(String name) {
		return null;
	}

	@Override
	public CompletableFuture<IGuildVoiceChannel> createVoiceChannel(String name, IOverwrite... overwrites) {
		return null;
	}

	@Override
	public Map<Long, IGuildChannel> getChannels() {
		Map<Long, IGuildChannel> channels = new HashMap<>();
		for (IGuildChannel channel : guild.getChannels().values()) {
			if (channel.inCategory(this)) channels.put(channel.getID(), channel);
		}
		return channels;
	}

	@Override
	public Map<Long, IGuildTextChannel> getTextChannels() {
		Map<Long, IGuildTextChannel> channels = new HashMap<>();
		for (IGuildTextChannel channel : getGuild().getTextChannels().values()) {
			if (channel.inCategory(this)) channels.put(channel.getID(), channel);
		}
		return channels;
	}

	@Override
	public Map<Long, IGuildVoiceChannel> getVoiceChannels() {
		Map<Long, IGuildVoiceChannel> channels = new HashMap<>();
		for (IGuildVoiceChannel channel : getGuild().getVoiceChannels().values()) {
			if (channel.inCategory(this)) channels.put(channel.getID(), channel);
		}
		return channels;
	}

	@Override
	public <T extends IGuildChannel> CompletableFuture<T> removeChannel(T channel) {
		CompletableFuture<T> future = new CompletableFuture<>();
		JSONObject settings = new JSONObject().put("parent_id", (String) null);
		loader.rest.request(Methods.PATCH, Endpoints.channel(channel.getID()), new RESTOptions(settings), ChannelJSON.class).thenAcceptAsync(data -> {
			@SuppressWarnings("unchecked")
			T newChannel = (T) EntityBuilder.getChannelFactory().buildChannel(data, guild, false);
			future.complete(newChannel);
		}).exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	public void setup(ChannelJSON data) {
		super.setup(data);
	}

}
