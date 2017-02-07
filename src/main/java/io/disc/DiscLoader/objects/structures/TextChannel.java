/**
 * 
 */
package io.disc.DiscLoader.objects.structures;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.disc.DiscLoader.objects.gateway.ChannelJSON;

/**
 * @author Perry Berman
 *
 */
public class TextChannel extends GuildChannel {

	public final HashMap<String, Message> messages;

	/**
	 * @param guild
	 * @param channel
	 */
	public TextChannel(Guild guild, ChannelJSON data) {
		super(guild, data);
		this.type = "text";

		this.messages = new HashMap<String, Message>();
	}

	public void setup(ChannelJSON data) {
		super.setup(data);

		this.topic = data.topic;

	}

	/**
	 * @param content the content to change the content to
	 * @return CompletableFuture<Message> a completable future 
	 */
	public CompletableFuture<Message> sendMessage(String content) {
		return this.loader.rest.sendMessage(this, content);
	}

}
