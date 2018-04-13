package io.discloader.discloader.entity.channel;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.voice.VoiceConnection;

public interface IVoiceChannel extends IChannel {

	/**
	 * Joins the voice channel.
	 * 
	 * @return A future that completes with the voice connection, if successful.
	 */
	public default CompletableFuture<VoiceConnection> join() {
		return new CompletableFuture<>();
	}

	/**
	 * Leaves the voice channel.
	 * 
	 * @return A future completes with the leave voice connection, if successful.
	 */
	public default CompletableFuture<VoiceConnection> leave() {
		return new CompletableFuture<>();
	}
}
