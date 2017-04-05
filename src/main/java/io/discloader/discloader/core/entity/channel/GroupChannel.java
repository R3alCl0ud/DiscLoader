package io.discloader.discloader.core.entity.channel;

import java.io.File;
import java.util.Collection;
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
import io.discloader.discloader.network.rest.actions.channel.pin.PinnedMessages;
import io.discloader.discloader.network.rest.actions.channel.pin.UnpinMessage;
import io.discloader.discloader.util.DLUtil.ChannelType;

/**
 * Represents a GroupDMChannel on discord. GroupChannels are only available for user accounts, not bot accounts.
 * 
 * @author Perry Berman
 */
public class GroupChannel extends Channel implements IGroupChannel, IVoiceChannel {
	
	/**
	 * A {@link HashMap} of the channel's {@link User recipients}. Indexed by {@link User#id}. <br>
	 * Is {@code null} if {@link #type} is {@code "text"} or {@code "voice"}.
	 * 
	 * @author Perry Berman
	 * @since 0.0.1
	 */
	private final HashMap<String, IUser> recipients;
	
	private final HashMap<String, IMessage> messages;
	
	private HashMap<String, IUser> typing;
	
	public GroupChannel(DiscLoader loader, ChannelJSON data) {
		super(loader, data);
		
		type = ChannelType.GROUPDM;
		
		messages = new HashMap<>();
		typing = new HashMap<>();
		recipients = new HashMap<>();
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
		return new BulkDelete<ITextChannel>(this, messages).execute();
	}
	
	@Override
	public CompletableFuture<IMessage> fetchMessage(String id) {
		return new FetchMessage<IGroupChannel>(this, id).execute();
	}
	
	@Override
	public CompletableFuture<Map<String, IMessage>> fetchMessages() {
		return fetchMessages(new MessageFetchOptions());
	}
	
	@Override
	public CompletableFuture<Map<String, IMessage>> fetchMessages(MessageFetchOptions options) {
		return new FetchMessages<IGroupChannel>(this, options).execute();
	}
	
	@Override
	public CompletableFuture<Map<String, IMessage>> fetchPinnedMessages() {
		return new PinnedMessages<IGroupChannel>(this).execute();
	}
	
	@Override
	public IMessage getMessage(String id) {
		return messages.get(id);
	}
	
	@Override
	public Collection<IMessage> getMessageCollection() {
		return getMessages().values();
	}
	
	@Override
	public HashMap<String, IMessage> getMessages() {
		return messages;
	}
	
	@Override
	public Map<String, IMessage> getPinnedMessages() {
		HashMap<String, IMessage> pins = new HashMap<>();
		for (IMessage message : messages.values()) {
			if (message.isPinned())
				pins.put(message.getID(), message);
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
	// public CompletableFuture<IMessage> pinMessage(IMessage message) {
	// return new PinMessage<T>(message).execute();
	// }
	
	@Override
	public <T extends ITextChannel> CompletableFuture<IMessage> pinMessage(IMessage message) {
		return null;
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
		return new SendMessage<IGroupChannel>(this, content, embed, attachment, file).execute();
	}
	

	@Override
	public CompletableFuture<Map<String, IUser>> startTyping() {
		return new StartTyping(this).execute();
	}
	@Override
	public CompletableFuture<IMessage> unpinMessage(IMessage message) {
		return new UnpinMessage<IGroupChannel>(message).execute();
	}
}
