package io.discloader.discloader.entity.message;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.exceptions.PermissionsException;
import io.discloader.discloader.core.entity.RichEmbed;
import io.discloader.discloader.core.entity.user.DLUser;
import io.discloader.discloader.entity.IEmoji;
import io.discloader.discloader.entity.channel.IGuildTextChannel;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.ICreationTime;
import io.discloader.discloader.entity.util.ISnowflake;
import io.discloader.discloader.entity.util.Permissions;
import io.discloader.discloader.network.json.MessageJSON;

/**
 * Represents a message object on the api <br>
 * <br>
 * <b>How to send messages</b>
 * 
 * <pre>
 * // Synchronously (Blocking)
 * IMessage message = iTextChannel.sendMessage(content).join();
 * 
 * // Asynchronously (Non-blocking)
 * iTextChannel.sendMessage(context).thenAccept(message -> {});
 * </pre>
 * 
 * @author Perry Berman
 * @since 0.1.2
 * @see ITextChannel#sendMessage(String, RichEmbed)
 * @see ISnowflake
 */
public interface IMessage extends ISnowflake, Comparable<IMessage>, ICreationTime {

	CompletableFuture<Void> addReaction(IReaction reaction);

	CompletableFuture<Void> addReaction(String unicode);

	CompletableFuture<Void> addReaction(IEmoji emoji);

	/**
	 * Checks if the user you are logged in as is able to delete the {@link IMessage
	 * message}. Should return {@code true} if either: you are the messsage's
	 * author, or you have the {@link Permissions#MANAGE_MESSAGES} permission.
	 * 
	 * @return {@code true} if you can delete the {@link IMessage message},
	 *         {@code false} otherwise.
	 */
	boolean canDelete();

	/**
	 * Only the {@link IMessage message's} {@link #getAuthor() author} is able to
	 * edit said {@link IMessage message}.
	 * 
	 * @return {@code true} if you are able to edit the {@link IMessage message} ,
	 *         otherwise {@code false}.
	 */
	boolean canEdit();

	/**
	 * Deletes the message if the loader has sufficient permissions
	 * 
	 * @see Permissions
	 * @return A Future that completes with {@literal this} when successful
	 */
	CompletableFuture<IMessage> delete();

	/**
	 * Removes all {@link IReaction reactions} from {@link IMessage this}
	 * message.<br>
	 * Requires the {@link Permissions#MANAGE_MESSAGES} permission.
	 * 
	 * @throws PermissionsException
	 *             Thrown if the current user doesn't have the
	 *             {@link Permissions#MANAGE_MESSAGES} permission.
	 * @return A Future that completes with {@literal this} when successful
	 */
	CompletableFuture<IMessage> deleteAllReactions();

	/**
	 * Edits the message's embed <br>
	 * An {@link IMessage}'s content can only be edited if the {@link DLUser user}
	 * you are logged in as is the message's {@link #getAuthor() author}.
	 * 
	 * @param embed
	 *            The message's new embed
	 * @return A CompletableFuture that completes with {@code this} if successful.
	 * @see #canEdit()
	 */
	CompletableFuture<IMessage> edit(RichEmbed embed);

	/**
	 * Edits the message's content.<br>
	 * An {@link IMessage}'s content can only be edited if the {@link DLUser user}
	 * you are logged in as is the message's {@link #getAuthor() author}.
	 * 
	 * @param content
	 *            The message's new content
	 * 
	 * @return A CompletableFuture that completes with {@code this} if successful.
	 * @see #canEdit()
	 */
	CompletableFuture<IMessage> edit(String content);

	/**
	 * Edits the message's content and embed. <br>
	 * An {@link IMessage}'s content can only be edited if the {@link DLUser user}
	 * you are logged in as is the message's {@link #getAuthor() author}.
	 * 
	 * @param embed
	 *            The message's new embed
	 * @param content
	 *            The message's new content
	 * @return A CompletableFuture that completes with {@code this} if successful.
	 * @see #canEdit()
	 */
	CompletableFuture<IMessage> edit(String content, RichEmbed embed);

	/**
	 * @return The activity
	 */
	IMessageActivity getActivity();

	/**
	 * @return The application
	 */
	IMessageApplication getApplication();

	/**
	 * Returns a {@link List} of {@link IMessageAttachment IMessageAttachments}.
	 * 
	 * @return A {@link List} of {@link IMessageAttachment IMessageAttachments}.
	 */
	List<IMessageAttachment> getAttachments();

	/**
	 * The {@link IUser user} who authored the message
	 * 
	 * @return An {@link IUser} object representing the {@link IMessage message's}
	 *         author.
	 */
	IUser getAuthor();

	/**
	 * Returns the {@link ITextChannel} the message was sent in.
	 * 
	 * @return The {@link ITextChannel} the message was sent in.
	 */
	ITextChannel getChannel();

	/**
	 * @return The message's content
	 */
	String getContent();

	OffsetDateTime getEditedAt();

	/**
	 * @return A List of {@link IMessageEmbed} objects sent with the message.
	 */
	List<IMessageEmbed> getEmbeds();

	IGuild getGuild();

	DiscLoader getLoader();

	/**
	 * If the {@link IMessage message} was sent in a {@link IGuildTextChannel} the
	 * {@link #getAuthor() author's} {@link IGuildMember} object should be returned.
	 * Unless the message was sent by a webhook
	 * 
	 * @return The member who sent the message. possibly {@code null}
	 */
	IGuildMember getMember();

	IMentions getMentions();

	/**
	 * Returns the message's nonce.
	 * 
	 * @return The message's nonce.
	 */
	String getNonce();

	/**
	 * Returns a List of {@link IReaction} Objects.
	 * 
	 * @return A List of {@link IReaction} Objects.
	 */
	List<IReaction> getReactions();

	IReaction getReaction(IEmoji emoji);

	IReaction getReaction(String unicode);

	/**
	 * Returns {@code true} if the message has been edited, {@code false} otherwise.
	 * 
	 * @return {@code true} if the message has been edited, {@code false} otherwise.
	 */
	boolean isEdited();

	/**
	 * @return {@code true} if pinned, otherwise {@code false}.
	 * @see ITextChannel#getPinnedMessages()
	 */
	boolean isPinned();

	/**
	 * Returns {@code true} if the message is a system message, {@code false}
	 * otherwise.
	 * 
	 * @return {@code true} if the message is a system message, {@code false}
	 *         otherwise.
	 */
	boolean isSystem();

	boolean isTTS();

	/**
	 * Pins the message to the {@link #getChannel() channel} it was sent in. <br>
	 * The {@link Permissions#MANAGE_MESSAGES} permission is required to pin a
	 * message.
	 * 
	 * @return A CompletableFuture that completes with {@code this} if successfull.
	 */
	CompletableFuture<IMessage> pin();

	CompletableFuture<IMessage> removeReaction(IReaction reaction);

	CompletableFuture<IMessage> removeReaction(IEmoji emoji);

	CompletableFuture<IMessage> removeReaction(String unicode);

	void setup(MessageJSON data);

	/**
	 * Unpins the message from the {@link #getChannel() channel} it was sent in.
	 * <br>
	 * The {@link Permissions#MANAGE_MESSAGES} permission is required to unpin a
	 * message.
	 * 
	 * @return A CompletableFuture that completes with {@code this} if successful.
	 */
	CompletableFuture<IMessage> unpin();
}
