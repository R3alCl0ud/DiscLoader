package io.discloader.discloader.core.entity.user;

import java.time.OffsetDateTime;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.client.render.texture.icon.UserIcon;
import io.discloader.discloader.client.render.util.IIcon;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.user.IUserProfile;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.UserJSON;
import io.discloader.discloader.network.rest.actions.FetchUserProfile;
import io.discloader.discloader.util.DLUtil;

/**
 * Represents a user on discord
 * 
 * @author Perry Berman
 */
public class User implements IUser {

	/**
	 * The loader instance that cached the user.
	 */
	public final DiscLoader loader;

	/**
	 * The user's unique Snowflake ID.
	 */
	private final long id;

	/**
	 * The user's username
	 */
	private String username;

	/**
	 * The hash of the user's avatar
	 */
	protected IIcon avatar;

	/**
	 * The user's four digit discriminator
	 */
	private String discriminator;

	/**
	 * Whether or not the user is a bot account
	 */
	public boolean bot;

	/**
	 * Whether or not the user has verified their email address
	 */
	private boolean verified;

	/**
	 * Whether or not the user has 2FA enabled
	 */
	private boolean mfa;

	public User(DiscLoader loader, UserJSON user) {
		this.loader = loader;

		this.id = SnowflakeUtil.parse(user.id);

		if (user.username != null) {
			this.setup(user);
		}
	}

	public User(IUser user) {
		this.loader = user.getLoader();

		this.id = user.getID();

		this.username = user.getUsername();

		this.discriminator = user.getDiscriminator();

		this.avatar = user.getAvatar();

		this.bot = user.isBot();
	}

	/**
	 * toStrings the user in mention format
	 * 
	 * @return {@literal <@}{@link #id this.id}{@literal>}
	 */
	@Override
	public String asMention() {
		return String.format("<@%s>", id);
	}

	@Override
	public UserIcon getAvatar() {
		return (UserIcon) avatar;
	}

	@Override
	public String getDiscriminator() {
		return discriminator;
	}

	/**
	 * @return the id
	 */
	@Override
	public long getID() {
		return id;
	}

	@Override
	public DiscLoader getLoader() {
		return loader;
	}

	/**
	 * @return A Future that completes with the user's profile if successful.
	 */
	@Override
	public CompletableFuture<IUserProfile> getProfile() {
		return new FetchUserProfile(this).execute();
	}

	/**
	 * @return the username
	 */
	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isBot() {
		return bot;
	}

	@Override
	public boolean isVerified() {
		return verified;
	}

	@Override
	public OffsetDateTime createdAt() {
		return DLUtil.creationTime(this);
	}

	@Override
	public boolean MFAEnabled() {
		return mfa;
	}

	@Override
	public void setup(UserJSON data) {
		if (data.username != null) this.username = data.username;

		if (data.discriminator != null) this.discriminator = data.discriminator;

		if (data.avatar != null) this.avatar = new UserIcon(this, data.avatar);

		if (data.bot == true || data.bot == false) this.bot = data.bot;
	}

	/**
	 * returns a String in the format of
	 * 
	 * <pre>
	 * username + "#" + discriminator
	 * </pre>
	 * 
	 * @return {@link #username}{@literal #}{@link #discriminator}
	 */
	@Override
	public String toString() {
		return String.format("%s#%s", username, discriminator);
	}

}
