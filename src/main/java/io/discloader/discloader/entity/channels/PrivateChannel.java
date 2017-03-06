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

/**
 * Represents a DM Channel on discord
 * 
 * @author Perry Berman
 */
public class PrivateChannel extends Channel implements ITextChannel {

	private HashMap<String, Message> messages;

	/**
	 * The user that this DM Channel was opened with
	 * 
	 * @author Perry Berman
	 */
	public User recipient;

	public PrivateChannel(DiscLoader loader, ChannelJSON data) {
		super(loader, data);
		this.type = ChannelType.DM;
		this.messages = new HashMap<>();
	}

	public void setup(ChannelJSON data) {
		super.setup(data);

	}

	@Override
	public Message getMessage(String id) {
		return this.messages.get(id);
	}

	@Override
	public HashMap<String, Message> getMessages() {
		return this.messages;
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
	public CompletableFuture<Message> pinMessage(Message message) {
		return null;
	}

	@Override
	public CompletableFuture<Message> unpinMessage(Message message) {
		return null;
	}

}