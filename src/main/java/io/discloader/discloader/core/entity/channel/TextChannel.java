package io.discloader.discloader.core.entity.channel;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.client.render.util.Resource;
import io.discloader.discloader.core.entity.RichEmbed;
import io.discloader.discloader.core.entity.guild.GuildMember;
import io.discloader.discloader.core.entity.message.Message;
import io.discloader.discloader.core.entity.message.MessageFetchOptions;
import io.discloader.discloader.entity.channel.IGuildTextChannel;
import io.discloader.discloader.entity.guild.IGuild;
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
	private final HashMap<Long, IMessage> messages;

	private HashMap<Long, IUser> typing;

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
	public CompletableFuture<Map<Long, IMessage>> deleteMessages(IMessage... messages) {
		HashMap<Long, IMessage> msgs = new HashMap<>();
		for (IMessage message : messages) {
			msgs.put(message.getID(), message);
		}
		return deleteMessages(msgs);
	}

	@Override
	public CompletableFuture<Map<Long, IMessage>> deleteMessages(Map<Long, IMessage> messages) {
		return new BulkDelete<IGuildTextChannel>(this, messages).execute();
	}

	@Override
	public CompletableFuture<IMessage> fetchMessage(long id) {
		return new FetchMessage<IGuildTextChannel>(this, id).execute();
	}

	@Override
	public CompletableFuture<Map<Long, IMessage>> fetchMessages() {
		return fetchMessages(new MessageFetchOptions());
	}

	@Override
	public CompletableFuture<Map<Long, IMessage>> fetchMessages(MessageFetchOptions options) {
		return new FetchMessages<IGuildTextChannel>(this, options).execute();
	}

	@Override
	public CompletableFuture<Map<Long, IMessage>> fetchPinnedMessages() {
		return new PinnedMessages<IGuildTextChannel>(this).execute();
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
			if (message.isPinned()) pins.put(message.getID(), message);
		}
		return pins;
	}

	/**
	 * @return the topic
	 */
	public String getTopic() {
		return topic;
	}

	@Override
	public Map<Long, IUser> getTyping() {
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
	public CompletableFuture<IMessage> pinMessage(IMessage message) {
		return new PinMessage<IGuildTextChannel>(message).execute();
	}

	@Override
	public CompletableFuture<IMessage> sendEmbed(RichEmbed embed) {
		if (embed.getThumbnail() != null && embed.getThumbnail().resource != null) return sendMessage(null, embed, (Resource) null);
		if (embed.getImage() != null && embed.getImage().resource != null) return sendMessage(null, embed, (Resource) null);
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
		if ((embed.getThumbnail() != null && embed.getThumbnail().resource != null)) return sendMessage(content, embed, (Resource) null);
		if ((embed.getImage() != null && embed.getImage().resource != null)) return sendMessage(content, embed, (Resource) null);
		return sendMessage(content, embed, (File) null);
	}

	@Override
	public CompletableFuture<IMessage> sendMessage(String content, RichEmbed embed, File file) {
		Attachment attachment = null;
		if (embed != null) {
			if (embed.getThumbnail() != null && embed.getThumbnail().file != null) {
				file = embed.getThumbnail().file;
				attachment = new Attachment(file.getName());
			} else if (embed.getImage() != null && embed.getImage().file != null) {
				file = embed.getImage().file;
				attachment = new Attachment(file.getName());
			}
		}
		return new SendMessage<IGuildTextChannel>(this, content, embed, attachment, file).execute();
	}

	@Override
	public CompletableFuture<IMessage> sendMessage(String content, RichEmbed embed, Resource resource) {
		Attachment attachment = null;
		if (embed != null) {
			if (embed.getThumbnail() != null && embed.getThumbnail().resource != null) {
				resource = embed.getThumbnail().resource;
				attachment = new Attachment(embed.getThumbnail().resource.getFileName());
			} else if (embed.getImage() != null && embed.getImage().resource != null) {
				resource = embed.getImage().resource;
				attachment = new Attachment(embed.getImage().resource.getFileName());
			}
		}
		return new SendMessage<IGuildTextChannel>(this, content, embed, attachment, resource).execute();
	}

	/**
	 * @param topic the topic to set
	 */
	public CompletableFuture<IGuildTextChannel> setTopic(String topic) {
		return null;
	}

	@Override
	public void setup(ChannelJSON data) {
		super.setup(data);

		this.type = ChannelType.TEXT;

		this.topic = data.topic;
	}

	@Override
	public CompletableFuture<Map<Long, IUser>> startTyping() {
		return new StartTyping(this).execute();
	}

	@Override
	public CompletableFuture<IMessage> unpinMessage(IMessage message) {
		return new UnpinMessage<IGuildTextChannel>(message).execute();
	}

	@Override
	public boolean isNSFW() {
		return getName().startsWith("nsfw-");
	}

	// @Override
	// public CompletableFuture<IGuildTextChannel> setNSFW(boolean nsfw) {
	// if (isNSFW()) return CompletableFuture.completedFuture(this);
	// return setName("nsfw-" + getName());
	// }

}
