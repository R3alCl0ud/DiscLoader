package io.discloader.discloader.core.entity.channel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.core.entity.RichEmbed;
import io.discloader.discloader.core.entity.message.MessageFetchOptions;
import io.discloader.discloader.entity.channel.IPrivateChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.sendable.Attachment;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.rest.actions.channel.BulkDelete;
import io.discloader.discloader.network.rest.actions.channel.FetchMessage;
import io.discloader.discloader.network.rest.actions.channel.FetchMessages;
import io.discloader.discloader.network.rest.actions.channel.SendMessage;
import io.discloader.discloader.network.rest.actions.channel.StartTyping;
import io.discloader.discloader.network.rest.actions.channel.close.CloseChannel;
import io.discloader.discloader.network.rest.actions.channel.pin.PinMessage;
import io.discloader.discloader.network.rest.actions.channel.pin.PinnedMessages;
import io.discloader.discloader.network.rest.actions.channel.pin.UnpinMessage;
import io.discloader.discloader.util.DLUtil.ChannelType;

/**
 * Represents a DM Channel on discord
 * 
 * @author Perry Berman
 */
public class PrivateChannel extends Channel implements IPrivateChannel {

	private HashMap<String, IMessage> messages;

	private HashMap<String, IUser> typing;

	/**
	 * The user that this DM Channel was opened with
	 * 
	 * @author Perry Berman
	 */
	private IUser recipient;

	public PrivateChannel(DiscLoader loader, ChannelJSON data) {
		super(loader, data);
		type = ChannelType.DM;
		messages = new HashMap<>();
		typing = new HashMap<>();
	}

	public CompletableFuture<IPrivateChannel> close() {
		return new CloseChannel<IPrivateChannel>(this).execute();
	}

	@Override
	public CompletableFuture<Map<String, IMessage>> deleteMessages(IMessage... messages) {
		HashMap<String, IMessage> msgs = new HashMap<>();
		for (IMessage message : messages) {
			msgs.put(message.getID(), message);
		}
		return deleteMessages(msgs);
	}

	@Override
	public CompletableFuture<Map<String, IMessage>> deleteMessages(Map<String, IMessage> messages) {
		return new BulkDelete<IPrivateChannel>(this, messages).execute();
	}

	@Override
	public CompletableFuture<IMessage> fetchMessage(String id) {
		return new FetchMessage<IPrivateChannel>(this, id).execute();
	}

	@Override
	public CompletableFuture<Map<String, IMessage>> fetchMessages() {
		return fetchMessages(new MessageFetchOptions());
	}

	@Override
	public CompletableFuture<Map<String, IMessage>> fetchMessages(MessageFetchOptions options) {
		return new FetchMessages<IPrivateChannel>(this, options).execute();
	}

	@Override
	public CompletableFuture<Map<String, IMessage>> fetchPinnedMessages() {
		return new PinnedMessages<IPrivateChannel>(this).execute();
	}

	@Override
	public IMessage getMessage(String id) {
		return messages.get(id);
	}

	@Override
	public HashMap<String, IMessage> getMessages() {
		return messages;
	}

	@Override
	public Map<String, IMessage> getPinnedMessages() {
		HashMap<String, IMessage> pins = new HashMap<>();
		for (IMessage message : messages.values()) {
			if (message.isPinned()) pins.put(message.getID(), message);
		}
		return pins;
	}

	@Override
	public IUser getRecipient() {
		return recipient;
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
	public CompletableFuture<IMessage> pinMessage(IMessage message) {
		return new PinMessage<IPrivateChannel>(message).execute();
	}

	@Override
	public CompletableFuture<IMessage> sendEmbed(RichEmbed embed) {
		return sendMessage(null, embed);
	}

	@Override
	public CompletableFuture<IMessage> sendMessage(String content) {
		return sendMessage(content, null);
	}

	@Override
	public CompletableFuture<IMessage> sendMessage(String content, RichEmbed embed) {
		File file = null;
		Attachment attachment = null;
		if (embed.thumbnail != null && embed.thumbnail.file != null) {
			file = embed.thumbnail.file;
			embed.thumbnail.file = null;
			attachment = new Attachment(file.getName());
		}
		return new SendMessage<IPrivateChannel>(this, content, embed, attachment, file).execute();
	}

	@Override
	public CompletableFuture<Map<String, IUser>> startTyping() {
		return new StartTyping(this).execute();
	}

	@Override
	public void setup(ChannelJSON data) {
		super.setup(data);
		if (data.recipients[0] != null) {
			recipient = loader.users.get(data.recipients[0].id);
			if (recipient == null) {
				recipient = loader.addUser(data.recipients[0]);
			}
		}
	}

	@Override
	public CompletableFuture<IMessage> unpinMessage(IMessage message) {
		return new UnpinMessage<IPrivateChannel>(message).execute();
	}

}