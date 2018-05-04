package io.discloader.discloader.core.entity.user;

import java.io.File;
import java.time.OffsetDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import io.discloader.discloader.client.render.util.Resource;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.core.entity.RichEmbed;
import io.discloader.discloader.entity.IIcon;
import io.discloader.discloader.entity.channel.IPrivateChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.user.IUserProfile;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.UserJSON;
import io.discloader.discloader.network.rest.actions.FetchUserProfile;
import io.discloader.discloader.network.rest.actions.channel.CreateDMChannel;

/**
 * Represents a user on discord
 * 
 * @author Perry Berman
 */
public class User implements IUser {

	/**
	 * The loader instance that cached the user.
	 */
	public DiscLoader loader;

	/**
	 * The user's unique Snowflake ID.
	 */
	private long id;

	/**
	 * The user's username
	 */
	private String username;

	/**
	 * The hash of the user's avatar
	 */
	protected String avatarHash;

	/**
	 * The user's four digit discriminator
	 */
	private short discriminator = 0;

	/**
	 * Whether or not the user is a bot account
	 */
	private boolean bot;

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

		this.id = SnowflakeUtil.parse(user.id == null ? "0" : user.id);

		if (user.username != null) {
			this.setup(user);
		}
	}

	public User(IUser user) {
		this.id = user.getID();
		this.loader = user.getLoader();
		username = user.getUsername() == null ? null : "" + user.getUsername();
		this.discriminator = user.getDiscriminator();
		avatarHash = user.getAvatar().getHash();
		this.bot = user.isBot();
		this.verified = user.isVerified();
		this.mfa = user.MFAEnabled();
	}

	/**
	 * 
	 */
	public User() {}

	/**
	 * toStrings the user in mention format
	 * 
	 * @return {@literal <@}{@link #id this.id}{@literal>}
	 */
	@Override
	public String toMention() {
		return String.format("<@%s>", id);
	}

	@Override
	public OffsetDateTime createdAt() {
		return SnowflakeUtil.creationTime(this);
	}

	// public IUser clone() {
	// User cloned = new User();
	// cloned.loader = this.loader;
	// // cloned.avatar = new String(this.avatar);
	// cloned.bot = this.bot;
	// cloned.discriminator = this.discriminator;
	// cloned.id = this.id;
	// cloned.username = this.username;
	// cloned.mfa = this.mfa;
	// cloned.verified = this.verified;
	// }

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof User))
			return false;
		User user = (User) object;
		if (this == user) {
			return true;
		}
		return id == user.id && username.equals(user.username) && discriminator == user.discriminator && mfa == user.mfa && verified == user.verified && isBot() == user.isBot();
	}

	@Override
	public IIcon getAvatar() {
		return new UserAvatar(avatarHash, id, discriminator);
	}

	@Override
	public short getDiscriminator() {
		return discriminator;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public DiscLoader getLoader() {
		return loader;
	}

	@Override
	public IPrivateChannel getPrivateChannel() {
		return EntityRegistry.getPrivateChannelByUser(this);
	}

	/**
	 * @return A Future that completes with the user's profile if successful.
	 */
	@Override
	public CompletableFuture<IUserProfile> getProfile() {
		return new FetchUserProfile(this).execute();
	}

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
	public boolean MFAEnabled() {
		return mfa;
	}

	@Override
	public CompletableFuture<IPrivateChannel> openPrivateChannel() {
		if (getPrivateChannel() != null)
			return CompletableFuture.completedFuture(getPrivateChannel());
		return new CreateDMChannel(this).execute();
	}

	@Override
	public CompletableFuture<IMessage> sendEmbed(RichEmbed embed) {
		if (embed.getThumbnail() != null && embed.getThumbnail().resource != null)
			return sendMessage(null, embed, (Resource) null);
		if (embed.getImage() != null && embed.getImage().resource != null)
			return sendMessage(null, embed, (Resource) null);
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
		if ((embed.getThumbnail() != null && embed.getThumbnail().resource != null))
			return sendMessage(content, embed, (Resource) null);
		if ((embed.getImage() != null && embed.getImage().resource != null))
			return sendMessage(content, embed, (Resource) null);
		return sendMessage(content, embed, (File) null);
	}

	@Override
	public CompletableFuture<IMessage> sendMessage(String content, RichEmbed embed, File file) {
		CompletableFuture<IMessage> future = new CompletableFuture<>();
		IPrivateChannel channel = getPrivateChannel();
		if (channel == null) {
			try {
				channel = openPrivateChannel().get();
				return channel.sendMessage(content, embed, file);
			} catch (ExecutionException | InterruptedException e) {
				future.completeExceptionally(e.getCause());
			}
			return future;
		}
		return channel.sendMessage(content, embed, file);
	}

	@Override
	public CompletableFuture<IMessage> sendMessage(String content, RichEmbed embed, Resource resource) {
		CompletableFuture<IMessage> future = new CompletableFuture<>();
		IPrivateChannel channel = getPrivateChannel();
		if (channel == null) {
			try {
				channel = openPrivateChannel().get();
				return channel.sendMessage(content, embed, resource);
			} catch (ExecutionException | InterruptedException e) {
				future.completeExceptionally(e.getCause());
				return future;
			}
		}
		return channel.sendMessage(content, embed, resource);
	}

	@Override
	public void setup(UserJSON data) {
		try {
			if (data.username != null)
				username = data.username;
			if (data.discriminator != null)
				discriminator = Short.parseShort(data.discriminator, 10);
			avatarHash = data.avatar;
			bot = data.bot;
			verified = data.verified;
			mfa = data.mfa_enabled;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return String.format("%s#%04d", username, discriminator);
	}

}
