package io.discloader.discloader.core.entity.channel;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.core.entity.RichEmbed;
import io.discloader.discloader.core.entity.message.Message;
import io.discloader.discloader.core.entity.message.MessageFetchOptions;
import io.discloader.discloader.core.entity.user.User;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.sendable.Attachment;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.rest.actions.channel.BulkDelete;
import io.discloader.discloader.network.rest.actions.channel.FetchMessage;
import io.discloader.discloader.network.rest.actions.channel.FetchMessages;
import io.discloader.discloader.network.rest.actions.channel.SendMessage;
import io.discloader.discloader.network.rest.actions.channel.StartTyping;
import io.discloader.discloader.network.rest.actions.channel.pin.PinMessage;
import io.discloader.discloader.network.rest.actions.channel.pin.PinnedMessages;
import io.discloader.discloader.network.rest.actions.channel.pin.UnpinMessage;
import io.discloader.discloader.util.DLUtil.ChannelType;

/**
 * Represents a GroupDMChannel on discord. GroupChannels are only available for
 * user accounts, not bot accounts.
 * 
 * @author Perry Berman
 */
public class GroupChannel extends Channel implements ITextChannel {

	/**
	 * A {@link HashMap} of the channel's {@link User recipients}. Indexed by
	 * {@link User#id}. <br>
	 * Is {@code null} if {@link #type} is {@code "text"} or {@code "voice"}.
	 * 
	 * @author Perry Berman
	 * @since 0.0.1
	 */
	private final HashMap<String, User> recipients;

	private final HashMap<String, Message> messages;

	private HashMap<String, IUser> typing;

	public GroupChannel(DiscLoader loader, ChannelJSON data) {
		super(loader, data);

		type = ChannelType.GROUPDM;

		messages = new HashMap<>();
		typing = new HashMap<>();
		recipients = new HashMap<>();
	}

	@Override
	public CompletableFuture<HashMap<String, Message>> deleteMessages(HashMap<String, Message> messages) {
		return new BulkDelete(this, messages).execute();
	}

	@Override
	public CompletableFuture<HashMap<String, Message>> deleteMessages(Message... messages) {
		HashMap<String, Message> msgs = new HashMap<>();
		for (Message message : messages) {
			msgs.put(message.id, message);
		}
		return deleteMessages(msgs);
	}

	@Override
	public CompletableFuture<Message> fetchMessage(String id) {
		return new FetchMessage(this, id).execute();
	}

	@Override
	public CompletableFuture<HashMap<String, Message>> fetchMessages() {
		return fetchMessages(new MessageFetchOptions());
	}

	@Override
	public CompletableFuture<HashMap<String, Message>> fetchMessages(MessageFetchOptions options) {
		return new FetchMessages(this, options).execute();
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
	public CompletableFuture<HashMap<String, Message>> fetchPinnedMessages() {
		return new PinnedMessages(this).execute();
	}

	@Override
	public HashMap<String, Message> getPinnedMessages() {
		HashMap<String, Message> pins = new HashMap<>();
		for (Message message : messages.values()) {
			if (message.isPinned()) pins.put(message.id, message);
		}
		return pins;
	}

	/**
	 * @return the recipients
	 */
	public HashMap<String, User> getRecipients() {
		return recipients;
	}

	@Override
	public HashMap<String, IUser> getTyping() {
		return typing;
	}

	@Override
	public boolean isTyping(IUser user) {
		return typing.containsKey(user.getID());
	}

	@Override
	public CompletableFuture<Message> pinMessage(Message message) {
		return new PinMessage(message).execute();
	}

	@Override
	public CompletableFuture<Message> sendEmbed(RichEmbed embed) {
		return sendMessage(null, embed);
	}

	@Override
	public CompletableFuture<Message> sendMessage(String content) {
		return sendMessage(content, null);
	}

	@Override
	public CompletableFuture<Message> sendMessage(String content, RichEmbed embed) {
		File file = null;
		Attachment attachment = null;
		if (embed.thumbnail != null && embed.thumbnail.file != null) {
			file = embed.thumbnail.file;
			embed.thumbnail.file = null;
			attachment = new Attachment(file.getName());
		}
		return new SendMessage(this, content, embed, attachment, file).execute();
	}

	@Override
	public CompletableFuture<HashMap<String, IUser>> startTyping() {
		return new StartTyping(this).execute();
	}

	@Override
	public CompletableFuture<Message> unpinMessage(Message message) {
		return new UnpinMessage(message).execute();
	}

}
