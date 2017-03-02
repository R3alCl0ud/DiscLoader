package io.discloader.discloader.entity.impl;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.voice.VoiceConnection;

public interface IVoiceChannel extends IChannel {
	CompletableFuture<VoiceConnection> join();

	CompletableFuture<VoiceConnection> leave();
}
