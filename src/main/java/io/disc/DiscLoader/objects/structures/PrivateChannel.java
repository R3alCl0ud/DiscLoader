package io.disc.DiscLoader.objects.structures;

import java.util.concurrent.CompletableFuture;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.objects.gateway.ChannelJSON;

public class PrivateChannel extends TextChannel {


	public PrivateChannel(DiscLoader loader, ChannelJSON data) {
		super(loader, data);
		this.type = "dm";
	}

	public void setup(ChannelJSON data) {
		super.setup(data);

	}

	/**
	 * @param content
	 *            the content to change the content to
	 * @return CompletableFuture<Message> a completable future
	 */
	public CompletableFuture<Message> sendMessage(String content) {
		return this.loader.rest.sendMessage(this, content);
	}

}
