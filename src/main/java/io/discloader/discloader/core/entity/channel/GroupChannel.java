package io.discloader.discloader.core.entity.channel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.core.entity.RichEmbed;
import io.discloader.discloader.core.entity.message.MessageFetchOptions;
import io.discloader.discloader.core.entity.user.User;
import io.discloader.discloader.entity.channel.IGroupChannel;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.channel.IVoiceChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.sendable.Attachment;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.voice.VoiceConnection;
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
public class GroupChannel extends Channel implements IGroupChannel, IVoiceChannel {

	/**
	 * A {@link HashMap} of the channel's {@link User recipients}. Indexed by
	 * {@link User#id}. <br>
	 * Is {@code null} if {@link #type} is {@code "text"} or {@code "voice"}.
	 * 
	 * @author Perry Berman
	 * @since 0.0.1
	 */
	private final HashMap<String, IUser> recipients;

	private final HashMap<String, IMessage<IGroupChannel>> messages;

	private HashMap<String, IUser> typing;

	public GroupChannel(DiscLoader loader, ChannelJSON data) {
		super(loader, data);

		type = ChannelType.GROUPDM;

		messages = new HashMap<>();
		typing = new HashMap<>();
		recipients = new HashMap<>();
	}

	@Override
	public CompletableFuture<Map<String, IMessage<ITextChannel>>> deleteMessages(IMessage<ITextChannel>[] messages) {
		HashMap<String, IMessage<ITextChannel>> msgs = new HashMap<>();
		for (IMessage<ITextChannel> message : messages) {
			msgs.put(message.getID(), message);
		}
		return deleteMessages(msgs);
	}

	@Override
	public CompletableFuture<Map<String, IMessage<ITextChannel>>> deleteMessages(Map<String, IMessage<ITextChannel>> messages) {
		return new BulkDelete<ITextChannel>(this, (Map<String, IMessage<ITextChannel>>) messages).execute();
	}

	@Override
	public CompletableFuture<IMessage<IGroupChannel>> fetchMessage(String id) {
		return new FetchMessage<IGroupChannel>(this, id).execute();
	}

	@Override
	public CompletableFuture<Map<String, IMessage<IGroupChannel>>> fetchMessages() {
		return fetchMessages(new MessageFetchOptions());
	}

	@Override
	public CompletableFuture<Map<String, IMessage<IGroupChannel>>> fetchMessages(MessageFetchOptions options) {
		return new FetchMessages<IGroupChannel>(this, options).execute();
	}

	@Override
	public CompletableFuture<Map<String, IMessage<IGroupChannel>>> fetchPinnedMessages() {
		return new PinnedMessages<IGroupChannel>(this).execute();
	}

	@Override
	public IMessage<IGroupChannel> getMessage(String id) {
		return messages.get(id);
	}

	@Override
	public HashMap<String, IMessage<IGroupChannel>> getMessages() {
		return messages;
	}

	@Override
	public Map<String, IMessage<IGroupChannel>> getPinnedMessages() {
		HashMap<String, IMessage<IGroupChannel>> pins = new HashMap<>();
		for (IMessage<IGroupChannel> message : messages.values()) {
			if (message.isPinned()) pins.put(message.getID(), message);
		}
		return pins;
	}

	@Override
	public Map<String, IUser> getRecipients() {
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
	public CompletableFuture<VoiceConnection> join() {
		return null;
	}

	@Override
	public CompletableFuture<VoiceConnection> leave() {
		return null;
	}
	//
	// @Override
	// public CompletableFuture<IMessage<T>> pinMessage(IMessage<T> message) {
	// return new PinMessage<T>(message).execute();
	// }

	@Override
	public CompletableFuture<IMessage<IGroupChannel>> sendEmbed(RichEmbed embed) {
		return sendMessage(null, embed);
	}

	@Override
	public CompletableFuture<IMessage<IGroupChannel>> sendMessage(String content) {
		return sendMessage(content, null);
	}

	@Override
	public CompletableFuture<IMessage<IGroupChannel>> sendMessage(String content, RichEmbed embed) {
		File file = null;
		Attachment attachment = null;
		if (embed.thumbnail != null && embed.thumbnail.file != null) {
			file = embed.thumbnail.file;
			embed.thumbnail.file = null;
			attachment = new Attachment(file.getName());
		}
		return new SendMessage<IGroupChannel>(this, content, embed, attachment, file).execute();
	}

	@Override
	public CompletableFuture<Map<String, IUser>> startTyping() {
		return new StartTyping(this).execute();
	}

	@Override
	public CompletableFuture<IMessage<IGroupChannel>> unpinMessage(IMessage<IGroupChannel> message) {
		return new UnpinMessage<IGroupChannel>(message).execute();
	}

	@Override
	public <T extends ITextChannel> CompletableFuture<IMessage<T>> unpinMessage(IMessage<T> message) {
		return null;
	}

	@Override
	public <T extends ITextChannel> CompletableFuture<IMessage<T>> pinMessage(IMessage<T> message) {
		return null;
	}
}
