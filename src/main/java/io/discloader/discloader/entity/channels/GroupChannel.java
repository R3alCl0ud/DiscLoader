package io.discloader.discloader.entity.channels;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.Attachment;
import io.discloader.discloader.entity.Message;
import io.discloader.discloader.entity.impl.ITextChannel;
import io.discloader.discloader.entity.sendable.RichEmbed;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.util.Constants.ChannelType;

public class GroupChannel extends Channel implements ITextChannel {
	private final HashMap<String, Message> messages;
	
	public GroupChannel(DiscLoader loader, ChannelJSON data) {
		super(loader, data);

		this.type = ChannelType.GROUPDM;
		
		this.messages = new HashMap<String, Message>();
	}

	/**
	 * Sends a {@link Message} to the channel.
	 * @param content The content to change the content to
	 * @return A Future that completes with a {@link Message} if successful,  
	 */
	public CompletableFuture<Message> sendMessage(String content) {
		return this.loader.rest.sendMessage(this, content, null, null, null);
	}

	/**
	 * Sends a {@link Message} to the channel.
	 * @param embed The embed to send
	 * @return A Future that completes with a {@link Message} if successful,  
	 */
	public CompletableFuture<Message> sendEmbed(RichEmbed embed) {
		File file = null;
		Attachment attachment = null;
		if (embed.thumbnail != null && embed.thumbnail.file != null) {
			file = embed.thumbnail.file;
			embed.thumbnail.file = null;
			attachment = new Attachment(file.getName());
		}
		return this.loader.rest.sendMessage(this, " ", embed, attachment, file);
	}

	/**
	 * Sends a {@link Message} to the channel.
	 * @param content The content to change the content to
	 * @return A Future that completes with a {@link Message} if successful,  
	 */
	public CompletableFuture<Message> sendMessage(String content, RichEmbed embed) {
		File file = null;
		Attachment attachment = null;
		if (embed.thumbnail != null && embed.thumbnail.file != null) {
			file = embed.thumbnail.file;
			embed.thumbnail.file = null;
			attachment = new Attachment(file.getName());
		}
		return this.loader.rest.sendMessage(this, content, embed, attachment, file);
	}

	@Override
	public Message getMessage(String id) {
		return this.messages.get(id);
	}

	@Override
	public HashMap<String, Message> getMessages() {
		return this.messages;
	}

}
