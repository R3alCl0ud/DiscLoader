package io.discloader.discloader.entity.channel;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.voice.VoiceConnection;

/**
 * @author Perry Berman
 */
public interface IGuildVoiceChannel extends IGuildChannel, IVoiceChannel {

	@Override
	public default CompletableFuture<VoiceConnection> join() {
		CompletableFuture<VoiceConnection> future = new CompletableFuture<>();
		if (EntityRegistry.getVoiceConnectionByID(getGuild().getID()) != null) {
			EntityRegistry.getVoiceConnectionByID(getGuild().getID()).disconnect().thenAcceptAsync(action -> {
				VoiceConnection connection = new VoiceConnection(this, future);
				EntityRegistry.addVoiceConnection(connection);
			});
			return future;
		}
		VoiceConnection connection = new VoiceConnection(this, future);
		EntityRegistry.addVoiceConnection(connection);
		return future;
	}

	int getBitrate();
	int getUserLimit();
	
}
