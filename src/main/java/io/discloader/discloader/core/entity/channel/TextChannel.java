package io.discloader.discloader.core.entity.channel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.core.entity.RichEmbed;
import io.discloader.discloader.core.entity.guild.GuildMember;
import io.discloader.discloader.core.entity.message.Message;
import io.discloader.discloader.core.entity.message.MessageFetchOptions;
import io.discloader.discloader.entity.channel.IGuildTextChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.message.IMessage;
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
 * Represents a TextChannel in a Guild on Discord
 * 
 * @author Perry Berman
 * @since 0.0.1
 * @version 3
 */
public class TextChannel extends GuildChannel implements IGuildTextChannel {

	/**
	 * A {@link HashMap} of the channel's cached messages. Indexed by
	 * {@link Message#id}.
	 * 
	 * @author Perry Berman
	 * @since 0.0.1
	 */
	private final HashMap<String, IMessage<IGuildTextChannel>> messages;

	private HashMap<String, IUser> typing;

	/**
	 * The channel's topic
	 * 
	 * @author Perry Berman
	 * @since 0.0.3
	 */
	private String topic;

	public TextChannel(IGuild guild, ChannelJSON data) {
		super(guild, data);

		messages = new HashMap<>();
		typing = new HashMap<>();
	}

	@Override
	@SuppressWarnings("unchecked")
	public CompletableFuture<Map<String, IMessage<IGuildTextChannel>>> deleteMessages(IMessage<IGuildTextChannel>... messages) {
		HashMap<String, IMessage<IGuildTextChannel>> msgs = new HashMap<>();
		for (IMessage<IGuildTextChannel> message : messages) {
			msgs.put(message.getID(), message);
		}
		return deleteMessages(msgs);
	}

	@Override
	public CompletableFuture<Map<String, IMessage<IGuildTextChannel>>> deleteMessages(Map<String, IMessage<IGuildTextChannel>> messages) {
		return new BulkDelete<IGuildTextChannel>(this, messages).execute();
	}

	@Override
	public CompletableFuture<IMessage<IGuildTextChannel>> fetchMessage(String id) {
		return new FetchMessage<IGuildTextChannel>(this, id).execute();
	}

	@Override
	public CompletableFuture<Map<String, IMessage<IGuildTextChannel>>> fetchMessages() {
		return fetchMessages(new MessageFetchOptions());
	}

	@Override
	public CompletableFuture<Map<String, IMessage<IGuildTextChannel>>> fetchMessages(MessageFetchOptions options) {
		return new FetchMessages<IGuildTextChannel>(this, options).execute();
	}

	@Override
	public CompletableFuture<Map<String, IMessage<IGuildTextChannel>>> fetchPinnedMessages() {
		return new PinnedMessages<IGuildTextChannel>(this).execute();
	}

	@Override
	public IMessage<IGuildTextChannel> getMessage(String id) {
		return messages.get(id);
	}

	@Override
	public Map<String, IMessage<IGuildTextChannel>> getMessages() {
		return messages;
	}

	@Override
	public Map<String, IMessage<IGuildTextChannel>> getPinnedMessages() {
		HashMap<String, IMessage<IGuildTextChannel>> pins = new HashMap<>();
		for (IMessage<IGuildTextChannel> message : messages.values()) {
			if (message.isPinned()) pins.put(message.getID(), message);
		}
		return pins;
	}

	@Override
	public Map<String, IUser> getTyping() {
		return typing;
	}

	/**
	 * Checks if a certain {@link GuildMember guild member} is typing in this
	 * channel
	 * 
	 * @param member The member to check.
	 * @return {@code true} if the member is typing, false otherwise.
	 */
	public boolean isTyping(GuildMember member) {
		return typing.containsKey(member.getID());
	}

	@Override
	public boolean isTyping(IUser user) {
		return typing.containsKey(user.getID());
	}

	@Override
	public CompletableFuture<IMessage<IGuildTextChannel>> pinMessage(IMessage<IGuildTextChannel> message) {
		return new PinMessage<IGuildTextChannel>(message).execute();
	}

	@Override
	public CompletableFuture<IMessage<IGuildTextChannel>> sendEmbed(RichEmbed embed) {
		return sendMessage(null, embed);
	}

	@Override
	public CompletableFuture<IMessage<IGuildTextChannel>> sendMessage(String content) {
		return sendMessage(content, null);
	}

	@Override
	public CompletableFuture<IMessage<IGuildTextChannel>> sendMessage(String content, RichEmbed embed) {
		File file = null;
		Attachment attachment = null;
		if (embed.thumbnail != null && embed.thumbnail.file != null) {
			file = embed.thumbnail.file;
			embed.thumbnail.file = null;
			attachment = new Attachment(file.getName());
		}
		return new SendMessage<IGuildTextChannel>(this, content, embed, attachment, file).execute();
	}

	@Override
	public void setup(ChannelJSON data) {
		super.setup(data);

		this.type = ChannelType.TEXT;

		this.topic = data.topic;
	}

	@Override
	public CompletableFuture<Map<String, IUser>> startTyping() {
		return new StartTyping(this).execute();
	}

	@Override
	public CompletableFuture<IMessage<IGuildTextChannel>> unpinMessage(IMessage<IGuildTextChannel> message) {
		return new UnpinMessage<IGuildTextChannel>(message).execute();
	}

	/**
	 * @return the topic
	 */
	public String getTopic() {
		return topic;
	}

	/**
	 * @param topic the topic to set
	 */
	public CompletableFuture<IGuildTextChannel> setTopic(String topic) {
		return null;
	}

}
