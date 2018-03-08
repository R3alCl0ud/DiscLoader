package io.discloader.discloader.entity.user;

import java.io.File;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.client.render.util.Resource;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.core.entity.RichEmbed;
import io.discloader.discloader.core.entity.message.Message;
import io.discloader.discloader.entity.IIcon;
import io.discloader.discloader.entity.IMentionable;
import io.discloader.discloader.entity.channel.IPrivateChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.util.ICreationTime;
import io.discloader.discloader.entity.util.ISnowflake;
import io.discloader.discloader.network.json.UserJSON;

/**
 * @author Perry Berman
 */
public interface IUser extends ISnowflake, ICreationTime, IMentionable {

	/**
	 * @return A String in discord's mention format
	 */
	String asMention();

	/**
	 * Returns an IIcon representation of the user's Avatar.
	 * 
	 * @return An IIcon representation of the user's Avatar.
	 */
	IIcon getAvatar();

	/**
	 * @return The user's four digit tag number
	 */
	short getDiscriminator();

	/**
	 * @return The {@link DiscLoader} object that was used to create the user object
	 */
	DiscLoader getLoader();

	IPrivateChannel getPrivateChannel();

	CompletableFuture<IUserProfile> getProfile();

	String getUsername();

	boolean isBot();

	boolean isVerified();

	boolean MFAEnabled();

	CompletableFuture<IPrivateChannel> openPrivateChannel();

	/**
	 * Sends a {@link Message} to the channel.
	 * 
	 * @param embed
	 *            The embed to send
	 * @return A Future that completes with a {@link Message} if successful,
	 */
	CompletableFuture<IMessage> sendEmbed(RichEmbed embed);

	CompletableFuture<IMessage> sendFile(File file);

	CompletableFuture<IMessage> sendFile(Resource resource);

	/**
	 * Sends a {@link Message} to the channel.
	 * 
	 * @param content
	 *            The message's content
	 * @return A Future that completes with a {@link Message} if successful,
	 */
	CompletableFuture<IMessage> sendMessage(String content);

	/**
	 * Sends a {@link Message} to the channel.
	 * 
	 * @param content
	 *            The message's content
	 * @param embed
	 *            The RichEmbed to send with the message
	 * @return A Future that completes with the pinned {@link Message} if
	 *         successful.
	 */
	CompletableFuture<IMessage> sendMessage(String content, RichEmbed embed);

	CompletableFuture<IMessage> sendMessage(String content, RichEmbed embed, File file);

	CompletableFuture<IMessage> sendMessage(String content, RichEmbed embed, Resource resource);

	void setup(UserJSON data);

	/**
	 * returns a String in the format of
	 * 
	 * <pre>
	 * username + "#" + discriminator
	 * </pre>
	 * 
	 * @return {@link #getUsername()}{@literal #}{@link #getDiscriminator()}
	 */
	String toString();

}
