package io.discloader.discloader.entity.channels;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.Attachment;
import io.discloader.discloader.entity.Message;
import io.discloader.discloader.entity.User;
import io.discloader.discloader.entity.impl.ITextChannel;
import io.discloader.discloader.entity.sendable.RichEmbed;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.util.DLUtil.ChannelType;

public class GroupChannel extends Channel implements ITextChannel {

	/**
	 * A {@link HashMap} of the channel's {@link User recipients}. Indexed by
	 * {@link User#id}. <br>
	 * Is {@code null} if {@link #type} is {@code "text"} or {@code "voice"}.
	 * 
	 * @author Perry Berman
	 * @since 0.0.1
	 */
	public final HashMap<String, User> recipients;

	private final HashMap<String, Message> messages;

	public GroupChannel(DiscLoader loader, ChannelJSON data) {
		super(loader, data);

		this.type = ChannelType.GROUPDM;

		this.messages = new HashMap<String, Message>();
		this.recipients = new HashMap<>();
	}

	public CompletableFuture<Message> sendMessage(String content) {
		return this.loader.rest.sendMessage(this, content, null, null, null);
	}

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

	@Override
	public CompletableFuture<Message> pinMessage(Message message) {
		return null;
	}

	@Override
	public CompletableFuture<Message> unpinMessage(Message message) {
		return null;
	}

}
