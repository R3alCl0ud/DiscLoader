package io.discloader.discloader.core.entity.channel;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.client.render.util.Resource;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.core.entity.RichEmbed;
import io.discloader.discloader.core.entity.message.Message;
import io.discloader.discloader.core.entity.user.User;
import io.discloader.discloader.entity.channel.IGroupChannel;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.channel.IVoiceChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.message.MessageFetchOptions;
import io.discloader.discloader.entity.sendable.Attachment;
import io.discloader.discloader.entity.sendable.SendableMessage;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.entity.voice.VoiceConnection;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.network.rest.RESTOptions;
import io.discloader.discloader.network.rest.actions.channel.BulkDelete;
import io.discloader.discloader.network.rest.actions.channel.FetchMessage;
import io.discloader.discloader.network.rest.actions.channel.FetchMessages;
import io.discloader.discloader.network.rest.actions.channel.StartTyping;
import io.discloader.discloader.network.rest.actions.channel.pin.PinMessage;
import io.discloader.discloader.network.rest.actions.channel.pin.PinnedMessages;
import io.discloader.discloader.network.rest.actions.channel.pin.UnpinMessage;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

/**
 * Represents a GroupDMChannel on discord. GroupChannels are only available for
 * user accounts, not bot accounts.
 * 
 * @author Perry Berman
 */
public class GroupChannel extends Channel implements IGroupChannel, IVoiceChannel {

	/**
	 * A {@link HashMap} of the channel's {@link User recipients}. Indexed by
	 * {@link User#id}. <br>
	 * Is {@code null} if {@link #type} is {@code "text"} or {@code "voice"}.
	 * 
	 * @author Perry Berman
	 * @since 0.0.1
	 */
	private final HashMap<Long, IUser> recipients;

	private final HashMap<Long, IMessage> messages;

	private HashMap<Long, IUser> typing;

	public GroupChannel(DiscLoader loader, ChannelJSON data) {
		super(loader, data);

		// type = ChannelType.GROUPDM;

		messages = new HashMap<>();
		typing = new HashMap<>();
		recipients = new HashMap<>();
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
		return new BulkDelete<ITextChannel>(this, messages).execute();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof GroupChannel))
			return false;
		GroupChannel groupChannel = (GroupChannel) o;
		return (this == groupChannel) || (groupChannel.getID() == getID());
	}

	@Override
	public CompletableFuture<IMessage> fetchMessage(long id) {
		return new FetchMessage<IGroupChannel>(this, id).execute();
	}

	@Override
	public CompletableFuture<Map<Long, IMessage>> fetchMessages() {
		return fetchMessages(new MessageFetchOptions());
	}

	@Override
	public CompletableFuture<Map<Long, IMessage>> fetchMessages(MessageFetchOptions options) {
		return new FetchMessages<IGroupChannel>(this, options).execute();
	}

	@Override
	public CompletableFuture<Map<Long, IMessage>> fetchPinnedMessages() {
		return new PinnedMessages<IGroupChannel>(this).execute();
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
	public Map<Long, IMessage> getMessages() {
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
	public Map<Long, IUser> getRecipients() {
		return recipients;
	}

	@Override
	public HashMap<Long, IUser> getTyping() {
		return typing;
	}

	@Override
	public int hashCode() {
		return Long.hashCode(getID());
	}

	@Override
	public boolean isTyping(IUser user) {
		return typing.containsKey(user.getID());
	}

	@Override
	public CompletableFuture<VoiceConnection> join() {
		return null;
	}

	@Override
	public CompletableFuture<VoiceConnection> leave() {
		return null;
	}

	@Override
	public CompletableFuture<IMessage> pinMessage(IMessage message) {
		return new PinMessage<IGroupChannel>(message).execute();
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
	public CompletableFuture<Map<Long, IUser>> startTyping() {
		return new StartTyping(this).execute();
	}

	@Override
	public CompletableFuture<IMessage> unpinMessage(IMessage message) {
		return new UnpinMessage<IGroupChannel>(message).execute();
	}
}
