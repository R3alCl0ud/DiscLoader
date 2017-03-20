package io.discloader.discloader.entity.channels;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.RichEmbed;
import io.discloader.discloader.entity.impl.ITextChannel;
import io.discloader.discloader.entity.message.Message;
import io.discloader.discloader.entity.message.MessageFetchOptions;
import io.discloader.discloader.entity.sendable.Attachment;
import io.discloader.discloader.entity.user.User;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.rest.actions.RESTFetchMessage;
import io.discloader.discloader.network.rest.actions.RESTFetchMessages;
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

	@Override
	public CompletableFuture<Message> fetchMessage(String id) {
		return new RESTFetchMessage(this, id).execute();
	}

	@Override
	public CompletableFuture<HashMap<String, Message>> fetchMessages() {
		return fetchMessages(new MessageFetchOptions());
	}

	@Override
	public CompletableFuture<HashMap<String, Message>> fetchMessages(MessageFetchOptions options) {
		return new RESTFetchMessages(this, options).execute();
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

	public CompletableFuture<Message> sendMessage(String content) {
		return this.loader.rest.sendMessage(this, content, null, null, null);
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
	public CompletableFuture<Message> unpinMessage(Message message) {
		return null;
	}

}
