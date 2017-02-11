package io.disc.discloader.objects.structures;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.disc.discloader.DiscLoader;
import io.disc.discloader.objects.gateway.ChannelJSON;

/**
 * @author Perry Berman
 *
 */
public class TextChannel extends Channel {
	/**
	 * A HashMap of the channel's cached messages. Indexed by 
	 */
	public final HashMap<String, Message> messages;

	/**
	 * @param guild
	 * @param data
	 */
	public TextChannel(Guild guild, ChannelJSON data) {
		super(guild, data);
		this.type = "text";

		this.messages = new HashMap<String, Message>();
	}

	public TextChannel(DiscLoader loader, ChannelJSON data) {
		super(loader, data);

		this.type = "dm";

		this.messages = new HashMap<String, Message>();
	}

	public void setup(ChannelJSON data) {
		super.setup(data);

		this.topic = data.topic;

	}

	/**
	 * @param content
	 *            the content to change the content to
	 * @return CompletableFuture
	 */
	public CompletableFuture<Message> sendMessage(String content) {
		return this.loader.rest.sendMessage(this, content);
	}

}
