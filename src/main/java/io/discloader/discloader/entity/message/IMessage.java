package io.discloader.discloader.entity.message;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.core.entity.RichEmbed;
import io.discloader.discloader.entity.channel.IGuildTextChannel;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.ISnowflake;
import io.discloader.discloader.entity.util.Permissions;
import io.discloader.discloader.network.json.MessageJSON;
import io.discloader.discloader.util.DLUtil;

/**
 * Represents a message object on the api <br>
 * <b>How to send messages</b>
 * 
 * <pre>
 * 
 * IMessage message = <ITextChannel>.sendMessage((String) content).join();
 * </pre>
 * 
 * @author Perry Berman
 * @since 0.1.2
 * @see ITextChannel#sendMessage(String, RichEmbed)
 * @see ISnowflake
 */
public interface IMessage extends ISnowflake, Comparable<IMessage> {

	/**
	 * Checks if the user you are logged in as is able to delete the
	 * {@link IMessage message}. Should return {@code true} if either: you are
	 * the messsage's author, or you have the
	 * {@link Permissions#MANAGE_MESSAGES} permission.
	 * 
	 * @return {@code true} if you can delete the {@link IMessage message},
	 *         {@code false} otherwise.
	 */
	boolean canDelete();

	/**
	 * Only the {@link IMessage message's} {@link #getAuthor() author} is able
	 * to edit said {@link IMessage message}.
	 * 
	 * @return {@code true} if you are able to edit the {@link IMessage message}
	 *         , otherwise {@code false}.
	 */
	boolean canEdit();

	OffsetDateTime createdAt();

	/**
	 * Deletes the message if the loader has suficient permissions
	 * 
	 * @see DLUtil.PermissionFlags
	 * @return A Future that completes with {@literal this} when sucessfull
	 */
	CompletableFuture<IMessage> delete();

	/**
	 * @param embed
	 * @return A CompletableFuture that completes with {@code this} if
	 *         successful.
	 * @see #canEdit()
	 */
	CompletableFuture<IMessage> edit(RichEmbed embed);

	/**
	 * @param embed
	 * @return A CompletableFuture that completes with {@code this} if
	 *         successful.
	 * @see #canEdit()
	 */
	CompletableFuture<IMessage> edit(String content);

	/**
	 * @param embed
	 * @return A CompletableFuture that completes with {@code this} if
	 *         successful.
	 * @see #canEdit()
	 */
	CompletableFuture<IMessage> edit(String content, RichEmbed embed);

	List<IMessageAttachment> getAttachments();

	/**
	 * The {@link IUser user} who authored the message
	 * 
	 * @return An {@link IUser} object representing the {@link IMessage
	 *         message's} author.
	 */
	IUser getAuthor();

	ITextChannel getChannel();

	String getContent();

	OffsetDateTime getEditedAt();

	/**
	 * @return A List of {@link IMessageEmbed} objects sent with the message.
	 */
	List<IMessageEmbed> getEmbeds();

	IGuild getGuild();

	DiscLoader getLoader();

	/**
	 * If the {@link IMessage message} was sent in a {@link IGuildTextChannel}
	 * the {@link #getAuthor() author's} {@link IGuildMember} object should be
	 * returned. Unless the message was sent by a webhook
	 * 
	 * @return The member who sent the message. possibly {@code null}
	 */
	IGuildMember getMember();

	IMentions getMentions();

	String getNonce();

	
	List<IReaction> getReactions();

	boolean isEdited();
	
	/**
	 * @return {@code true} if pinned, otherwise {@code false}.
	 * @see ITextChannel#getPinnedMessages()
	 */
	boolean isPinned();

	/**
	 * @return
	 */
	boolean isSystem();

	boolean isTTS();

	CompletableFuture<IMessage> pin();

	void setup(MessageJSON data);

	CompletableFuture<IMessage> unpin();
}
