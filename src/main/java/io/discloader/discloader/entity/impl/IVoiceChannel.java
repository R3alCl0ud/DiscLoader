package io.discloader.discloader.entity.impl;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.channels.VoiceChannel;

public interface IVoiceChannel {
	CompletableFuture<?> join();
	
	CompletableFuture<VoiceChannel> leave();
}
