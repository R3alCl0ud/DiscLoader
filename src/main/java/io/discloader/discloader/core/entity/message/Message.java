package io.discloader.discloader.core.entity.message;

import java.io.File;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.exceptions.PermissionsException;
import io.discloader.discloader.common.registry.EntityBuilder;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.core.entity.RichEmbed;
import io.discloader.discloader.core.entity.channel.Channel;
import io.discloader.discloader.core.entity.message.embed.MessageEmbed;
import io.discloader.discloader.entity.IEmoji;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.channel.IGuildTextChannel;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.message.IMentions;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.message.IMessageAttachment;
import io.discloader.discloader.entity.message.IMessageEmbed;
import io.discloader.discloader.entity.message.IReaction;
import io.discloader.discloader.entity.sendable.Attachment;
import io.discloader.discloader.entity.sendable.SendableMessage;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.ISnowflake;
import io.discloader.discloader.entity.util.Permissions;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.EmbedJSON;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.network.json.ReactionJSON;
import io.discloader.discloader.network.json.UserJSON;
import io.discloader.discloader.network.rest.RESTOptions;
import io.discloader.discloader.network.rest.actions.channel.pin.PinMessage;
import io.discloader.discloader.network.rest.actions.channel.pin.UnpinMessage;
import io.discloader.discloader.network.rest.actions.message.CreateReaction;
import io.discloader.discloader.network.rest.actions.message.DeleteReaction;
import io.discloader.discloader.network.util.Endpoints;
import io.discloader.discloader.network.util.Methods;

public class Message<T extends ITextChannel> implements IMessage {

	private List<IMessageAttachment> attachments;

	/**
	 * The message's {@link ISnowflake Snowflake} ID.
	 */
	private final long id;

	/**
	 * The message's content
	 */
	private String content;

	/**
	 * The time at which the message has been edited. is null if the message has not
	 * been edited
	 */
	public String edited_timestamp;

	/**
	 * Used for checking if the message has been sent
	 */
	public String nonce;

	/**
	 * The id of the webhook that sent the message. {@code null} if sent by a
	 * bot/user account
	 */
	public String webhookID;

	/**
	 * Whether or not the message was sent using /tts
	 */
	private boolean tts;

	private boolean pinned;

	/**
	 * The time at which the message was sent
	 */
	private String timestamp;

	/**
	 * The time at which the message was lasted edited at
	 */
	// private String editedAt;

	/**
	 * An object containing the information about who was mentioned in the message
	 */
	public Mentions mentions;

	/**
	 * The current instance of DiscLoader
	 */
	public final DiscLoader loader;

	/**
	 * The channel the message was sent in
	 */
	public final T channel;

	public final IUser author;

	/**
	 * The guild the {@link #channel} is in. is {@code null} if {@link Channel#type}
	 * is "dm" or "groupDM"
	 */
	public IGuild guild;

	public IGuildMember member;

	private int type;

	private List<IMessageEmbed> embeds;

	private List<IReaction> reactions;

	/**
	 * Creates a new message object
	 * 
	 * @param channel
	 *            The channel the message was sent in
	 * @param data
	 *            The message's data
	 */
	public Message(T channel, MessageJSON data) {
		id = SnowflakeUtil.parse(data.id);

		this.channel = channel;

		loader = DiscLoader.getDiscLoader();
		if (channel != null && channel instanceof IGuildTextChannel) {
			guild = ((IGuildTextChannel) channel).getGuild();
		}

		if (data.author != null) {
			if (!EntityRegistry.userExists(data.author.id)) {
				author = EntityRegistry.addUser(data.author);
			} else {
				author = EntityRegistry.getUserByID(data.author.id);
			}
		} else {
			UserJSON wh = new UserJSON();
			wh.id = data.webhook_id == null ? "0" : data.webhook_id;
			author = EntityRegistry.addUser(wh);
		}
		embeds = new ArrayList<>();
		attachments = new ArrayList<>();
		reactions = new ArrayList<>();
		content = data.content;

		setup(data);
	}

	@Override
	public CompletableFuture<Void> addReaction(IEmoji emoji) {
		return addReaction(SnowflakeUtil.asString(emoji));
	}

	@Override
	public CompletableFuture<Void> addReaction(IReaction reaction) {
		return addReaction(reaction.getEmoji());
	}

	@Override
	public CompletableFuture<Void> addReaction(String unicode) {
		return new CreateReaction(this, unicode).execute();
	}

	@Override
	public boolean canDelete() {
		return canEdit();
	}

	@Override
	public boolean canEdit() {
		return loader.user.getID() == author.getID();
	}

	@Override
	public int compareTo(IMessage message) {
		return message.getContent().compareTo(getContent());
	}

	@Override
	public OffsetDateTime createdAt() {
		return OffsetDateTime.parse(timestamp);
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Message<?>))
			return false;
		Message<?> message = (Message<?>) object;
		return (this == message) || getID() == message.getID();
	}

	@Override
	public int hashCode() {
		return Long.hashCode(getID());
	}

	@Override
	public CompletableFuture<IMessage> delete() {
		CompletableFuture<IMessage> future = new CompletableFuture<>();
		CompletableFuture<Void> cf = loader.rest.request(Methods.DELETE, Endpoints.message(getChannel().getID(), getID()), new RESTOptions(), Void.class);
		cf.thenAcceptAsync(Null -> {
			future.complete(this);
		});
		cf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	@Override
	public CompletableFuture<IMessage> deleteAllReactions() {
		CompletableFuture<IMessage> future = new CompletableFuture<>();
		if (channel instanceof IGuildChannel) {
			if (!((IGuildChannel) channel).permissionsOf(guild.getCurrentMember()).hasPermission(Permissions.MANAGE_MESSAGES)) {
				future.completeExceptionally(new PermissionsException());
				return future;
			}
		}
		CompletableFuture<Void> cf = loader.rest.request(Methods.DELETE, Endpoints.messageReactions(getChannel().getID(), getID()), new RESTOptions(), Void.class);
		cf.thenAcceptAsync(Null -> {
			future.complete(this);
		});
		cf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	/**
	 * Edit's the messages content. Only possible if the {@link DiscLoader loader}
	 * is the message's {@link #author}
	 * 
	 * @param embed
	 *            The new embed for the message
	 * @return A Future that completes with {@literal this} when sucessfull
	 */
	@Override
	public CompletableFuture<IMessage> edit(RichEmbed embed) {
		return this.edit(null, embed);
	}

	/**
	 * Edit's the messages content. Only possible if the {@link DiscLoader loader}
	 * is the message's {@link #author}
	 * 
	 * @param content
	 *            The new content of the message
	 * @return A Future that completes with {@literal this} when sucessfull
	 */
	@Override
	public CompletableFuture<IMessage> edit(String content) {
		return this.edit(content, null);
	}

	/**
	 * Edit's the messages content. Only possible if the {@link DiscLoader loader}
	 * is the message's {@link #author}
	 * 
	 * @param content
	 *            The new content of the message
	 * @param embed
	 *            The new embed for the message
	 * @return A Future that completes with {@literal this} when sucessfull
	 */
	@Override
	public CompletableFuture<IMessage> edit(String content, RichEmbed embed) {
		CompletableFuture<IMessage> future = new CompletableFuture<>();
		if (!canEdit()) {
			future.completeExceptionally(new PermissionsException("Only messages author by you can be editted"));
			return future;
		}

		SendableMessage sendable = null;
		Attachment attachment = null;
		File file = null;
		if (embed != null) {
			if ((embed.getThumbnail() != null && embed.getThumbnail().resource != null)) {
				attachment = new Attachment(embed.getThumbnail().resource.getName());
			}
			if (embed.getThumbnail() != null && embed.getThumbnail().file != null) {
				attachment = new Attachment(embed.getThumbnail().file.getName());
			}
			if ((embed.getImage() != null && embed.getImage().resource != null)) {
				attachment = new Attachment(embed.getImage().resource.getName());
			}
			if (embed.getImage() != null && embed.getImage().file != null) {
				attachment = new Attachment(embed.getImage().file.getName());
			}
		}
		sendable = new SendableMessage(content, false, embed, attachment, file);
		CompletableFuture<MessageJSON> cf = loader.rest.request(Methods.PATCH, Endpoints.message(getChannel().getID(), getID()), new RESTOptions(sendable), MessageJSON.class);
		cf.thenAcceptAsync(messageJSON -> {
			future.complete(EntityBuilder.getChannelFactory().buildMessage(channel, messageJSON));
		});

		cf.exceptionally(ex -> {
			future.completeExceptionally(ex);
			return null;
		});
		return future;
	}

	@Override
	public List<IMessageAttachment> getAttachments() {
		return attachments;
	}

	@Override
	public IUser getAuthor() {
		return author;
	}

	@Override
	public T getChannel() {
		return channel;
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public OffsetDateTime getEditedAt() {
		return edited_timestamp == null ? null : OffsetDateTime.parse(edited_timestamp);
	}

	@Override
	public List<IMessageEmbed> getEmbeds() {
		return embeds;
	}

	@Override
	public IGuild getGuild() {
		return guild;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public DiscLoader getLoader() {
		return channel.getLoader();
	}

	@Override
	public IGuildMember getMember() {
		return guild.getMember(author.getID());
	}

	@Override
	public IMentions getMentions() {
		return mentions;
	}

	@Override
	public String getNonce() {
		return nonce;
	}

	@Override
	public List<IReaction> getReactions() {
		return reactions;
	}

	/**
	 * Whether or not you can edit the message.
	 * 
	 * @return {@code true} when {@link #author}.id equals {@link #loader} .user.id,
	 *         {@code false} otherwise.
	 * @since 0.1.0
	 */
	public boolean isEditable() {
		return loader.user.getID() == author.getID();
	}

	@Override
	public boolean isEdited() {
		return edited_timestamp != null;
	}

	/**
	 * Is the messaged pinned in the {@link #channel}
	 * 
	 * @return {@code true} if {@code this} is pinned, {@code false} otherwise.
	 */
	@Override
	public boolean isPinned() {
		return pinned;
	}

	/**
	 * Checks if the message was sent by a user or if it is a system message. <br>
	 * Ex: "user has pinned a message to this channel." would be a system message
	 * 
	 * @return true if the message is a system message, false otherwise.
	 * @since 0.1.0
	 */
	@Override
	public boolean isSystem() {
		return this.type != 0;
	}

	/**
	 * @return true if {@code this} was sent using /tts
	 */
	@Override
	public boolean isTTS() {
		return tts;
	}

	/**
	 * Pins {@code this} to the {@link #channel}
	 * 
	 * @return A Future that completes with {@code this} if successful.
	 */
	@Override
	public CompletableFuture<IMessage> pin() {
		CompletableFuture<IMessage> future = new PinMessage<T>(this).execute();
		future.thenAcceptAsync(action -> this.pinned = true);
		return future;
	}

	@Override
	public CompletableFuture<IMessage> removeReaction(IEmoji emoji) {
		return removeReaction(SnowflakeUtil.asString(emoji));
	}

	@Override
	public CompletableFuture<IMessage> removeReaction(IReaction reaction) {
		return removeReaction(reaction.getEmoji());
	}

	@Override
	public CompletableFuture<IMessage> removeReaction(String unicode) {
		return new DeleteReaction(this, unicode).execute();
	}

	@Override
	public void setup(MessageJSON data) {

		mentions = new Mentions(this, data.mentions, data.mention_roles, data.mention_everyone);

		timestamp = data.timestamp;

		edited_timestamp = data.edited_timestamp;

		tts = data.tts;

		content = data.content;

		nonce = data.nonce;
		if (data.embeds != null) {
			for (EmbedJSON em : data.embeds) {
				embeds.add(new MessageEmbed(em));
			}
		}

		type = data.type;
		if (data.reactions != null) {
			for (ReactionJSON r : data.reactions) {
				reactions.add(new Reaction(r, this));
			}
		}
	}

	/**
	 * Unpins {@code this} from the {@link #channel}
	 * 
	 * @return A Future that completes with {@code this} if successful.
	 */
	@Override
	public CompletableFuture<IMessage> unpin() {
		CompletableFuture<IMessage> future = new UnpinMessage<T>(this).execute();
		future.thenAcceptAsync(action -> this.pinned = false);
		return future;
	}

	@Override
	public IReaction getReaction(IEmoji emoji) {
		return getReaction(SnowflakeUtil.asString(emoji));
	}

	@Override
	public IReaction getReaction(String unicode) {
		for (IReaction reaction : reactions) {
			if (unicode.equals(SnowflakeUtil.asString(reaction.getEmoji())))
				return reaction;
		}
		return null;
	}
}
