package io.discloader.discloader.entity.user;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.client.render.util.IRenderable;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.util.ICreationTime;
import io.discloader.discloader.entity.util.ISnowflake;
import io.discloader.discloader.network.json.UserJSON;

/**
 * @author Perry Berman
 */
public interface IUser extends ISnowflake, ICreationTime {

	/**
	 * @return A String in discord's mention format
	 */
	String asMention();

	/**
	 * @param user
	 * @return
	 */
	boolean equals(IUser user);

	/**
	 * @return
	 */
	IRenderable getAvatar();

	/**
	 * @return
	 */
	String getDiscriminator();

	/**
	 * @return
	 */
	DiscLoader getLoader();

	CompletableFuture<IUserProfile> getProfile();

	String getUsername();

	boolean isBot();

	boolean isVerified();

	boolean MFAEnabled();

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
