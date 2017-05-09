package io.discloader.discloader.core.entity.channel;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.client.render.util.Resource;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.core.entity.RichEmbed;
import io.discloader.discloader.core.entity.message.MessageFetchOptions;
import io.discloader.discloader.entity.channel.IPrivateChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.sendable.Attachment;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.SnowflakeUtil;
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

	private HashMap<Long, IMessage> messages;

	private HashMap<Long, IUser> typing;

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
	public CompletableFuture<Map<Long, IMessage>> deleteMessages(IMessage... messages) {
		HashMap<Long, IMessage> msgs = new HashMap<>();
		for (IMessage message : messages) {
			msgs.put(message.getID(), message);
		}
		return deleteMessages(msgs);
	}

	@Override
	public CompletableFuture<Map<Long, IMessage>> deleteMessages(Map<Long, IMessage> messages) {
		return new BulkDelete<IPrivateChannel>(this, messages).execute();
	}

	@Override
	public CompletableFuture<IMessage> fetchMessage(long id) {
		return new FetchMessage<IPrivateChannel>(this, id).execute();
	}

	@Override
	public CompletableFuture<Map<Long, IMessage>> fetchMessages() {
		return fetchMessages(new MessageFetchOptions());
	}

	@Override
	public CompletableFuture<Map<Long, IMessage>> fetchMessages(MessageFetchOptions options) {
		return new FetchMessages<IPrivateChannel>(this, options).execute();
	}

	@Override
	public CompletableFuture<Map<Long, IMessage>> fetchPinnedMessages() {
		return new PinnedMessages<IPrivateChannel>(this).execute();
	}

	@Override
	public IMessage getMessage(long messageID) {
		return messages.get(messageID);
	}

	@Override
	public IMessage getMessage(String id) {
		return getMessage(SnowflakeUtil.parse(id));
	}

	@Override
	public Collection<IMessage> getMessageCollection() {
		return getMessages().values();
	}

	@Override
	public HashMap<Long, IMessage> getMessages() {
		return messages;
	}

	@Override
	public Map<Long, IMessage> getPinnedMessages() {
		HashMap<Long, IMessage> pins = new HashMap<>();
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
	public HashMap<Long, IUser> getTyping() {
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
		return sendMessage(null, embed, (File) null);
	}

	@Override
	public CompletableFuture<IMessage> sendFile(File file) {
		return sendMessage(null, null, file);
	}

	@Override
	public CompletableFuture<IMessage> sendFile(Resource resource) {
		return sendMessage(null, null, resource);
	}

	@Override
	public CompletableFuture<IMessage> sendMessage(String content) {
		return sendMessage(content, null, (File) null);
	}

	@Override
	public CompletableFuture<IMessage> sendMessage(String content, RichEmbed embed) {
		return sendMessage(content, embed, (File) null);
	}

	@Override
	public CompletableFuture<IMessage> sendMessage(String content, RichEmbed embed, File file) {
		Attachment attachment = null;
		if (file != null) attachment = new Attachment(file.getName());
		if (embed != null && embed.thumbnail != null && embed.thumbnail.file != null) {
			file = embed.thumbnail.file;
			embed.thumbnail.file = null;
			attachment = new Attachment(file.getName());
		}
		return new SendMessage<IPrivateChannel>(this, content, embed, attachment, file).execute();
	}

	@Override
	public CompletableFuture<IMessage> sendMessage(String content, RichEmbed embed, Resource resource) {
		Attachment attachment = null;
		if (embed.thumbnail != null && embed.thumbnail.resource != null) {
			attachment = new Attachment(embed.thumbnail.resource.getFileName());
		} else if (embed.getImage() != null && embed.getImage().resource != null) {
			attachment = new Attachment(embed.getImage().resource.getFileName());
		}
		return new SendMessage<IPrivateChannel>(this, content, embed, attachment, resource).execute();
	}

	@Override
	public void setup(ChannelJSON data) {
		super.setup(data);
		if (data.recipients[0] != null) {
			recipient = EntityRegistry.getUserByID(data.recipients[0].id);
			if (recipient == null) {
				recipient = EntityRegistry.addUser(data.recipients[0]);
			}
		}
	}

	@Override
	public CompletableFuture<Map<Long, IUser>> startTyping() {
		return new StartTyping(this).execute();
	}

	@Override
	public CompletableFuture<IMessage> unpinMessage(IMessage message) {
		return new UnpinMessage<IPrivateChannel>(message).execute();
	}
}