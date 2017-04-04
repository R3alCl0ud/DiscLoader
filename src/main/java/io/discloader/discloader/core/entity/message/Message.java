package io.discloader.discloader.core.entity.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.core.entity.RichEmbed;
import io.discloader.discloader.core.entity.channel.Channel;
import io.discloader.discloader.core.entity.channel.PrivateChannel;
import io.discloader.discloader.core.entity.channel.TextChannel;
import io.discloader.discloader.entity.ISnowflake;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.message.IMentions;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.message.IMessageAttachment;
import io.discloader.discloader.entity.message.IMessageEmbed;
import io.discloader.discloader.entity.message.IReaction;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.network.rest.actions.channel.pin.PinMessage;
import io.discloader.discloader.network.rest.actions.channel.pin.UnpinMessage;
import io.discloader.discloader.network.rest.actions.message.DeleteMessage;
import io.discloader.discloader.network.rest.actions.message.EditMessage;
import io.discloader.discloader.util.DLUtil;

public class Message<T extends ITextChannel> implements IMessage {

	private List<IMessageAttachment> attachments;

	/**
	 * The message's {@link ISnowflake Snowflake} ID.
	 */
	private final String id;

	/**
	 * The message's content
	 */
	private String content;

	/**
	 * The time at which the message has been edited. is null if the message has
	 * not been edited
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
	private Date timestamp;

	/**
	 * The time at which the message was lasted edited at
	 */
	private Date editedAt;

	/**
	 * An object containing the information about who was mentioned in the
	 * message
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

	/**
	 * The user who authored the message
	 */
	public final IUser author;

	/**
	 * The guild the {@link #channel} is in. is {@code null} if
	 * {@link Channel#type} is "dm" or "groupDM"
	 */
	public IGuild guild;

	public IGuildMember member;

	private int type;

	public ArrayList<IMessageEmbed> embeds;

	/**
	 * Creates a new message object
	 * 
	 * @param channel The channel the message was sent in
	 * @param data The message's data
	 */
	public Message(T channel, MessageJSON data) {
		id = data.id;

		this.channel = channel;

		if (channel.isPrivate()) {
			PrivateChannel privateChannel = (PrivateChannel) channel;
			loader = privateChannel.getLoader();
		} else {
			TextChannel textChannel = (TextChannel) channel;
			loader = textChannel.getLoader();
			guild = textChannel.getGuild();
		}

		if (!loader.users.containsKey(data.author.id)) {
			author = loader.addUser(data.author);
		} else {
			author = loader.users.get(data.author.id);
		}

		embeds = new ArrayList<>();
		attachments = new ArrayList<>();

		setup(data);
	}

	@Override
	public boolean canDelete() {
		return canEdit();
	}

	@Override
	public boolean canEdit() {
		return loader.user.getID().equals(author.getID());
	}

	/**
	 * Deletes the message if the loader has suficient permissions
	 * 
	 * @see DLUtil.PermissionFlags
	 * @return A Future that completes with {@literal this} when sucessfull
	 */
	@Override
	public CompletableFuture<IMessage> delete() {
		return new DeleteMessage<T>(this.channel, this).execute();
	}

	/**
	 * Edit's the messages content. Only possible if the {@link DiscLoader
	 * loader} is the message's {@link #author}
	 * 
	 * @param embed The new embed for the message
	 * @return A Future that completes with {@literal this} when sucessfull
	 */
	@Override
	public CompletableFuture<IMessage> edit(RichEmbed embed) {
		return this.edit(null, embed);
	}

	/**
	 * Edit's the messages content. Only possible if the {@link DiscLoader
	 * loader} is the message's {@link #author}
	 * 
	 * @param content The new content of the message
	 * @return A Future that completes with {@literal this} when sucessfull
	 */
	@Override
	public CompletableFuture<IMessage> edit(String content) {
		return this.edit(content, null);
	}

	/**
	 * Edit's the messages content. Only possible if the {@link DiscLoader
	 * loader} is the message's {@link #author}
	 * 
	 * @param content The new content of the message
	 * @param embed The new embed for the message
	 * @return A Future that completes with {@literal this} when sucessfull
	 */
	@Override
	public CompletableFuture<IMessage> edit(String content, RichEmbed embed) {
		return new EditMessage<T>(this, content, embed, null, null).execute();
	}

	@Override
	public List<IMessageAttachment> getAttachments() {
		return null;
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
	public java.util.Date getEditedTimestamp() {
		return editedAt;
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
	public String getID() {
		return id;
	}

	@Override
	public DiscLoader getLoader() {
		return loader;
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
		return null;
	}

	@Override
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * Whether or not you can edit the message.
	 * 
	 * @return {@code true} when {@link #author}.id equals {@link #loader}
	 *         .user.id, {@code false} otherwise.
	 * @since 0.1.0
	 */
	public boolean isEditable() {
		return loader.user.getID().equals(author.getID());
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
	 * Checks if the message was sent by a user or if it is a system message.
	 * <br>
	 * Ex: "user has pinned a message to this channel." would be a system
	 * message
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
	public void setup(MessageJSON data) {

		mentions = new Mentions(this, data.mentions, data.mention_roles, data.mention_everyone);

		timestamp = DLUtil.parseISO8601(data.timestamp);

		editedAt = data.edited_timestamp != null ? DLUtil.parseISO8601(data.edited_timestamp) : null;

		tts = data.tts;

		content = data.content;

		nonce = data.nonce;

		type = data.type;

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
}
