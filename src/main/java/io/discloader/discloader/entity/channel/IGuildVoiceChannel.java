package io.discloader.discloader.entity.channel;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.voice.VoiceConnect;

/**
 * @author Perry Berman
 */
public interface IGuildVoiceChannel extends IGuildChannel, IVoiceChannel {

	public default CompletableFuture<VoiceConnect> join() {
		CompletableFuture<VoiceConnect> future = new CompletableFuture<>();
		if (EntityRegistry.getVoiceConnectionByID(getGuild().getID()) != null) {
			EntityRegistry.getVoiceConnectionByID(getGuild().getID()).disconnect().thenAcceptAsync(action -> {
				VoiceConnect connection = new VoiceConnect(this, future);
				EntityRegistry.putVoiceConnection(connection);
			});
			return future;
		}
		VoiceConnect connection = new VoiceConnect(this, future);
		EntityRegistry.putVoiceConnection(connection);
		return future;
	}

}
