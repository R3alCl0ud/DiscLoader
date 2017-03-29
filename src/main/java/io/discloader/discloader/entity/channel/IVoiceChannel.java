package io.discloader.discloader.entity.channel;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.voice.VoiceConnection;

public interface IVoiceChannel extends IChannel {
	/**
	 * Joins a voice channel.
	 * @return A future that completes with the voice connection, if successful.
	 */
	CompletableFuture<VoiceConnection> join();

	/**
	 * Leaves a voice channel
	 * @return A future completes with the leave voice connection, if successful.
	 */
	CompletableFuture<VoiceConnection> leave();
}
