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
import io.discloader.discloader.core.entity.message.Message;
import io.discloader.discloader.entity.channel.IPrivateChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.message.MessageFetchOptions;
import io.discloader.discloader.entity.sendable.Attachment;
import io.discloader.discloader.entity.sendable.SendableMessage;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.network.rest.RESTOptions;
import io.discloader.discloader.network.rest.actions.channel.BulkDelete;
import io.discloader.discloader.network.rest.actions.channel.FetchMessage;
import io.discloader.discloader.network.rest.actions.channel.FetchMessages;
import io.discloader.discloader.network.rest.actions.channel.StartTyping;
import io.discloader.discloader.network.rest.actions.channel.close.CloseChannel;
import io.discloader.discloader.network.rest.actions.channel.pin.PinMessage;
import io.discloader.discloader.network.rest.actions.channel.pin.PinnedMessages;
import io.discloader.discloader.network.rest.actions.channel.pin.UnpinMessage;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

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
		type = 1;
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
	public boolean equals(Object object) {
		if (!(object instanceof PrivateChannel))
			return false;
		PrivateChannel channel = (PrivateChannel) object;
		return (this == channel) || getID() == channel.getID();
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
	public IMessage getLastMessage() {
		return getMessage(getLastMessageID());
	}

	@Override
	public long getLastMessageID() {
		long lastMsgID = 0l;
		for (IMessage message : messages.values()) {
			if ((message.getID() >> 22) > (lastMsgID >> 22))
				lastMsgID = message.getID();
		}
		return lastMsgID;
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
			if (message.isPinned())
				pins.put(message.getID(), message);
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
		// System.out.println(DLUtil.gson.toJson(embed));
		if (embed.getThumbnail() != null && embed.getThumbnail().resource != null)
			return sendMessage(null, embed, embed.getThumbnail().resource);
		if (embed.getThumbnail() != null && embed.getThumbnail().file != null)
			return sendMessage(null, embed, embed.getThumbnail().file);
		if (embed.getImage() != null && embed.getImage().resource != null)
			return sendMessage(null, embed, embed.getImage().resource);
		if (embed.getImage() != null && embed.getImage().file != null)
			return sendMessage(null, embed, embed.getImage().file);
		return sendMessage(null, embed, (File) null);
	}

	@Override
	public CompletableFuture<IMessage> sendFile(Attachment attachment) {
		return sendMessage(null, null, attachment);
	}

	@Override
	public CompletableFuture<IMessage> sendFile(Attachment attachment, String content) {
		return sendMessage(content, null, attachment);
	}

	@Override
	public CompletableFuture<IMessage> sendFile(File file) {
		return sendMessage(null, null, file);
	}

	@Override
	public CompletableFuture<IMessage> sendFile(File file, String content) {
		return sendMessage(content, null, file);
	}

	@Override
	public CompletableFuture<IMessage> sendFile(Resource resource) {
		return sendMessage(null, null, resource);
	}

	@Override
	public CompletableFuture<IMessage> sendFile(Resource resource, String content) {
		return sendMessage(content, null, resource);
	}

	@Override
	public CompletableFuture<IMessage> sendMessage(String content) {
		return sendMessage(content, null, (File) null, false);
	}

	@Override
	public CompletableFuture<IMessage> sendMessage(String content, boolean tts) {
		return sendMessage(content, null, (File) null, tts);
	}

	@Override
	public CompletableFuture<IMessage> sendMessage(String content, RichEmbed embed) {
		return sendMessage(content, embed, false);
	}

	@Override
	public CompletableFuture<IMessage> sendMessage(String content, RichEmbed embed, Attachment attachment) {
		return sendMessage(content, embed, attachment, false);
	}

	@Override
	public CompletableFuture<IMessage> sendMessage(String content, RichEmbed embed, Attachment attachment, boolean tts) {
		CompletableFuture<IMessage> future = new CompletableFuture<>();
		File file = attachment == null ? null : new File(attachment.filename);
		SendableMessage sendable = new SendableMessage(content, tts, embed, attachment, file);
		CompletableFuture<MessageJSON> mcf = loader.rest.request(Methods.POST, Endpoints.messages(getID()), new RESTOptions(sendable), MessageJSON.class);
		mcf.thenAcceptAsync(e -> {
			future.complete(new Message<>(this, e));
		});
		mcf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	@Override
	public CompletableFuture<IMessage> sendMessage(String content, RichEmbed embed, boolean tts) {
		if (embed != null) {
			if ((embed.getThumbnail() != null && embed.getThumbnail().resource != null))
				return sendMessage(content, embed, embed.getThumbnail().resource, tts);
			if (embed.getThumbnail() != null && embed.getThumbnail().file != null)
				return sendMessage(content, embed, embed.getThumbnail().file, tts);
			if ((embed.getImage() != null && embed.getImage().resource != null))
				return sendMessage(content, embed, embed.getImage().resource, tts);
			if (embed.getImage() != null && embed.getImage().file != null)
				return sendMessage(content, embed, embed.getImage().file, tts);
		}
		return sendMessage(content, embed, (File) null, tts);
	}

	@Override
	public CompletableFuture<IMessage> sendMessage(String content, RichEmbed embed, File file) {
		return sendMessage(content, embed, file, false);

	}

	@Override
	public CompletableFuture<IMessage> sendMessage(String content, RichEmbed embed, File file, boolean tts) {
		Attachment attachment = file == null ? null : new Attachment(file.getName());
		SendableMessage sendable = new SendableMessage(content, tts, embed, attachment, file);
		CompletableFuture<IMessage> future = new CompletableFuture<>();
		CompletableFuture<MessageJSON> mcf = loader.rest.request(Methods.POST, Endpoints.messages(getID()), new RESTOptions(sendable), MessageJSON.class);
		mcf.thenAcceptAsync(e -> {
			future.complete(new Message<>(this, e));
		});
		mcf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	@Override
	public CompletableFuture<IMessage> sendMessage(String content, RichEmbed embed, Resource resource) {
		return sendMessage(content, embed, resource, false);
	}

	@Override
	public CompletableFuture<IMessage> sendMessage(String content, RichEmbed embed, Resource resource, boolean tts) {
		Attachment attachment = resource == null ? null : new Attachment(resource.getFileName());
		SendableMessage sendable = new SendableMessage(content, tts, embed, attachment, resource);
		CompletableFuture<IMessage> future = new CompletableFuture<>();
		CompletableFuture<MessageJSON> mcf = loader.rest.request(Methods.POST, Endpoints.messages(getID()), new RESTOptions(sendable), MessageJSON.class);
		mcf.thenAcceptAsync(e -> {
			future.complete(new Message<>(this, e));
		});
		mcf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	@Override
	public CompletableFuture<IMessage> sendTTSMessage(String content) {
		return sendMessage(content, null, (File) null, true);
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