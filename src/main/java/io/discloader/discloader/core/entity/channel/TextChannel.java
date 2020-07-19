package io.discloader.discloader.core.entity.channel;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;

import io.discloader.discloader.client.render.util.Resource;
import io.discloader.discloader.common.exceptions.PermissionsException;
import io.discloader.discloader.common.registry.EntityBuilder;
import io.discloader.discloader.core.entity.guild.GuildMember;
import io.discloader.discloader.core.entity.message.Message;
import io.discloader.discloader.core.entity.message.embed.RichEmbed;
import io.discloader.discloader.entity.channel.IGuildTextChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.message.MessageFetchOptions;
import io.discloader.discloader.entity.sendable.Attachment;
import io.discloader.discloader.entity.sendable.SendableMessage;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.Permissions;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.network.rest.RESTOptions;
import io.discloader.discloader.network.rest.actions.channel.BulkDelete;
import io.discloader.discloader.network.rest.actions.channel.FetchMessage;
import io.discloader.discloader.network.rest.actions.channel.FetchMessages;
import io.discloader.discloader.network.rest.actions.channel.pin.PinMessage;
import io.discloader.discloader.network.rest.actions.channel.pin.PinnedMessages;
import io.discloader.discloader.network.rest.actions.channel.pin.UnpinMessage;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

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

	private boolean nsfw;

	private int rateLimit;

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
	public CompletableFuture<IGuildTextChannel> edit(String name, int position) {
		if (!this.permissionsOf(guild.getCurrentMember()).hasAny(Permissions.MANAGE_CHANNELS, Permissions.ADMINISTRATOR)
				&& !guild.isOwner()) {
			throw new PermissionsException("Insufficient Permissions");
		}

		if (name.length() < 2 || name.length() > 100) {
			throw new RuntimeException("Name.length() out of bounds [2-100]");
		}

		CompletableFuture<IGuildTextChannel> future = new CompletableFuture<>();
		JSONObject payload = new JSONObject().put("name", sanitizeChannelName(name)).put("position", position);
		CompletableFuture<ChannelJSON> cf = loader.rest.request(Methods.PATCH, Endpoints.channel(getID()),
				new RESTOptions(payload), ChannelJSON.class);
		cf.thenAcceptAsync(data -> {
			IGuildTextChannel channel = (IGuildTextChannel) EntityBuilder.getChannelFactory().buildChannel(data,
					getLoader(), getGuild(), false);
			future.complete(channel);
		});
		cf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	@Override
	public CompletableFuture<IGuildTextChannel> edit(String name, String topic) {
		if (!this.permissionsOf(guild.getCurrentMember()).hasAny(Permissions.MANAGE_CHANNELS, Permissions.ADMINISTRATOR)
				&& !guild.isOwner()) {
			throw new PermissionsException("Insufficient Permissions");
		}

		if (name.length() < 2 || name.length() > 100) {
			throw new RuntimeException("Name.length() out of bounds [2-100]");
		}

		if (topic.length() > 1024) {
			throw new RuntimeException("Topic.length() out of bounds [" + topic.length() + " > 1024]");
		}

		CompletableFuture<IGuildTextChannel> future = new CompletableFuture<>();
		JSONObject payload = new JSONObject().put("name", sanitizeChannelName(name)).put("topic", topic);
		CompletableFuture<ChannelJSON> cf = loader.rest.request(Methods.PATCH, Endpoints.channel(getID()),
				new RESTOptions(payload), ChannelJSON.class);
		cf.thenAcceptAsync(data -> {
			IGuildTextChannel channel = (IGuildTextChannel) EntityBuilder.getChannelFactory().buildChannel(data,
					getLoader(), getGuild(), false);
			future.complete(channel);
		});
		cf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	@Override
	public CompletableFuture<IGuildTextChannel> edit(String name, String topic, int position) {
		if (!this.permissionsOf(guild.getCurrentMember()).hasAny(Permissions.MANAGE_CHANNELS, Permissions.ADMINISTRATOR)
				&& !guild.isOwner()) {
			throw new PermissionsException("Insufficient Permissions");
		}

		if (name.length() < 2 || name.length() > 100) {
			throw new RuntimeException("Name.length() out of bounds [2-100]");
		}

		if (topic.length() > 1024) {
			throw new RuntimeException("Topic.length() out of bounds [" + topic.length() + " > 1024]");
		}

		CompletableFuture<IGuildTextChannel> future = new CompletableFuture<>();
		JSONObject payload = new JSONObject().put("name", sanitizeChannelName(name)).put("topic", topic)
				.put("position", position);
		//.put("rate_limit_per_user", Math.min(21600, Math.max(rateLimit, 0)));
		CompletableFuture<ChannelJSON> cf = loader.rest.request(Methods.PATCH, Endpoints.channel(getID()),
				new RESTOptions(payload), ChannelJSON.class);
		cf.thenAcceptAsync(data -> {
			IGuildTextChannel channel = (IGuildTextChannel) EntityBuilder.getChannelFactory().buildChannel(data,
					getLoader(), getGuild(), false);
			future.complete(channel);
		});
		cf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	@Override
	public CompletableFuture<IGuildTextChannel> edit(String name, String topic, int position, int rateLimit) {
		if (!this.permissionsOf(guild.getCurrentMember()).hasAny(Permissions.MANAGE_CHANNELS, Permissions.ADMINISTRATOR)
				&& !guild.isOwner()) {
			throw new PermissionsException("Insufficient Permissions");
		}

		if (name.length() < 2 || name.length() > 100) {
			throw new RuntimeException("Name.length() out of bounds [2-100]");
		}

		if (topic.length() > 1024) {
			throw new RuntimeException("Topic.length() out of bounds [" + topic.length() + " > 1024]");
		}

		CompletableFuture<IGuildTextChannel> future = new CompletableFuture<>();
		JSONObject payload = new JSONObject().put("name", sanitizeChannelName(name)).put("topic", topic)
				.put("position", position).put("rate_limit_per_user", Math.min(21600, Math.max(rateLimit, 0)));
		CompletableFuture<ChannelJSON> cf = loader.rest.request(Methods.PATCH, Endpoints.channel(getID()),
				new RESTOptions(payload), ChannelJSON.class);
		cf.thenAcceptAsync(data -> {
			IGuildTextChannel channel = (IGuildTextChannel) EntityBuilder.getChannelFactory().buildChannel(data,
					getLoader(), getGuild(), false);
			future.complete(channel);
		});
		cf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof TextChannel))
			return false;
		TextChannel channel = (TextChannel) object;

		return (this == channel) || (getID() == channel.getID());

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
	public int getRateLimit() {
		if (getGuild().getMember(getLoader().user.getID()).getPermissions().hasPermission(Permissions.MANAGE_MESSAGES,
				Permissions.MANAGE_CHANNELS)) {
			return 0;
		}
		return rateLimit;
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

	@Override
	public boolean isNSFW() {
		return nsfw;
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
	public CompletableFuture<IMessage> sendMessage(String content, RichEmbed embed, Attachment attachment,
			boolean tts) {
		CompletableFuture<IMessage> future = new CompletableFuture<>();
		File file = attachment == null ? null : new File(attachment.filename);
		SendableMessage sendable = new SendableMessage(content, tts, embed, attachment, file);
		CompletableFuture<MessageJSON> mcf = loader.rest.request(Methods.POST, Endpoints.messages(getID()),
				new RESTOptions(sendable), MessageJSON.class);
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
		CompletableFuture<MessageJSON> mcf = loader.rest.request(Methods.POST, Endpoints.messages(getID()),
				new RESTOptions(sendable), MessageJSON.class);
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
		CompletableFuture<MessageJSON> mcf = loader.rest.request(Methods.POST, Endpoints.messages(getID()),
				new RESTOptions(sendable), MessageJSON.class);
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
	public CompletableFuture<IGuildTextChannel> setNSFW(boolean nsfw) {
		if (!this.permissionsOf(guild.getCurrentMember()).hasAny(Permissions.MANAGE_CHANNELS, Permissions.ADMINISTRATOR)
				&& !guild.isOwner()) {
			throw new PermissionsException("Insufficient Permissions");
		}

		CompletableFuture<IGuildTextChannel> future = new CompletableFuture<>();
		JSONObject payload = new JSONObject().put("nsfw", nsfw);
		CompletableFuture<ChannelJSON> cf = loader.rest.request(Methods.PATCH, Endpoints.channel(getID()),
				new RESTOptions(payload), ChannelJSON.class);
		cf.thenAcceptAsync(data -> {
			IGuildTextChannel channel = (IGuildTextChannel) EntityBuilder.getChannelFactory().buildChannel(data,
					getLoader(), getGuild(), false);
			future.complete(channel);
		});
		cf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	@Override
	public CompletableFuture<IGuildTextChannel> setRateLimit(int rate) {
		// TODO Auto-generated method stub
		return null;
	}

	public CompletableFuture<IGuildTextChannel> setTopic(String topic) {
		if (!this.permissionsOf(guild.getCurrentMember()).hasAny(Permissions.MANAGE_CHANNELS, Permissions.ADMINISTRATOR)
				&& !guild.isOwner()) {
			throw new PermissionsException("Insufficient Permissions");
		}

		if (topic.length() > 1024) {
			throw new RuntimeException("topic length [" + topic.length() + "] > 1024");
		}

		CompletableFuture<IGuildTextChannel> future = new CompletableFuture<>();
		JSONObject payload = new JSONObject().put("topic", topic);
		CompletableFuture<ChannelJSON> cf = loader.rest.request(Methods.PATCH, Endpoints.channel(getID()),
				new RESTOptions(payload), ChannelJSON.class);
		cf.thenAcceptAsync(data -> {
			IGuildTextChannel channel = (IGuildTextChannel) EntityBuilder.getChannelFactory().buildChannel(data,
					getLoader(), getGuild(), false);
			future.complete(channel);
		});
		cf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	@Override
	public void setup(ChannelJSON data) {
		super.setup(data);

		// type = ChannelType.TEXT;
		nsfw = data.nsfw;
		topic = data.topic;
		rateLimit = data.rate_limit_per_user;
	}

	@Override
	public CompletableFuture<Map<Long, IUser>> startTyping() {
		CompletableFuture<Map<Long, IUser>> future = new CompletableFuture<>();
		CompletableFuture<Void> cf = loader.rest.request(Methods.POST, Endpoints.channelTyping(getID()),
				new RESTOptions(), Void.class);
		cf.thenAcceptAsync(n -> {
			typing.put(loader.user.getID(), loader.user);
			future.complete(typing);
		});
		cf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.discloader.discloader.entity.IMentionable#asMention()
	 */
	@Override
	public String toMention() {
		return String.format("<#%d>", getID());
	}

	@Override
	public CompletableFuture<IMessage> unpinMessage(IMessage message) {
		return new UnpinMessage<IGuildTextChannel>(message).execute();
	}

}
