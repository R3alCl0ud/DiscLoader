package io.discloader.discloader.network.rest.actions.guild;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.channel.IGuildVoiceChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.sendable.SendableChannel;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.rest.actions.RESTAction;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

public class CreateVoiceChannel extends RESTAction<IGuildVoiceChannel> {

	private IGuild guild;
	private SendableChannel data;

	public CreateVoiceChannel(IGuild guild, SendableChannel data) {
		super(guild.getLoader());
		this.guild = guild;
		this.data = data;
	}

	@Override
	public CompletableFuture<IGuildVoiceChannel> execute() {
		return super.execute(loader.rest.makeRequest(Endpoints.guildChannels(guild.getID()), Methods.POST, true, data));
	}

	public void complete(String packet, Throwable ex) {
		if (ex != null) {
			future.completeExceptionally(ex);
			return;
		}

		ChannelJSON data = gson.fromJson(packet, ChannelJSON.class);
		future.complete((IGuildVoiceChannel) EntityRegistry.addChannel(data,loader));
	}

}
