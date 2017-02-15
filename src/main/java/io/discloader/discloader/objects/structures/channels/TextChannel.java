package io.discloader.discloader.objects.structures.channels;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.DiscLoader;
import io.discloader.discloader.objects.gateway.ChannelJSON;
import io.discloader.discloader.objects.structures.Guild;
import io.discloader.discloader.objects.structures.Message;
import io.discloader.discloader.objects.structures.RichEmbed;

/**
 * @author Perry Berman
 *
 */
public class TextChannel extends Channel {
	/**
	 * A {@link HashMap} of the channel's cached messages. Indexed by {@link Message#id}.
	 * @author Perry Berman
	 * @since 0.0.1
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
	 * Sends a {@link Message} to the channel.
	 * @param content The content to change the content to
	 * @return A Future that completes with a {@link Message} if successfull,  
	 */
	public CompletableFuture<Message> sendMessage(String content) {
		return this.loader.rest.sendMessage(this, content, null);
	}

	/**
	 * Sends a {@link Message} to the channel.
	 * @param content The content to change the content to
	 * @return A Future that completes with a {@link Message} if successfull,  
	 */
	public CompletableFuture<Message> sendEmbed(RichEmbed embed) {
		return this.loader.rest.sendMessage(this, null, embed);
	}
	
}
