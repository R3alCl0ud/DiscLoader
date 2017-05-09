package io.discloader.discloader.core.entity.user;

import java.io.File;
import java.time.OffsetDateTime;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.client.render.util.Resource;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.core.entity.RichEmbed;
import io.discloader.discloader.entity.IIcon;
import io.discloader.discloader.entity.channel.IPrivateChannel;
import io.discloader.discloader.entity.message.IMessage;
import io.discloader.discloader.entity.sendable.Attachment;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.user.IUserProfile;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.UserJSON;
import io.discloader.discloader.network.rest.actions.FetchUserProfile;
import io.discloader.discloader.network.rest.actions.channel.CreateDMChannel;
import io.discloader.discloader.network.rest.actions.channel.SendMessage;

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
	protected String avatar;

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

		this.id = SnowflakeUtil.parse(user.id == null ? "0" : user.id);

		if (user.username != null) {
			this.setup(user);
		}
	}

	public User(IUser user) {
		this.loader = user.getLoader();

		this.id = user.getID();

		this.username = user.getUsername();

		this.discriminator = user.getDiscriminator();

		this.avatar = user.getAvatar().getHash();

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
	public OffsetDateTime createdAt() {
		return SnowflakeUtil.creationTime(this);
	}

	@Override
	public boolean equals(IUser user) {
		return this == user && id == user.getID() && username.equals(user.getUsername()) && discriminator.equals(user.getDiscriminator());
	}

	@Override
	public IIcon getAvatar() {
		return new UserAvatar(avatar, id);
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
	public boolean MFAEnabled() {
		return mfa;
	}

	@Override
	public CompletableFuture<IPrivateChannel> openPrivateChannel() {
		if (getPrivateChannel() != null) return CompletableFuture.completedFuture(getPrivateChannel());
		return new CreateDMChannel(this).execute();
	}

	@Override
	public void setup(UserJSON data) {
		if (data.username != null) username = data.username;

		if (data.discriminator != null) discriminator = data.discriminator;

		if (data.avatar != null) avatar = data.avatar;

		if (data.bot == true || data.bot == false) bot = data.bot;
	}

	@Override
	public String toString() {
		return String.format("%s#%s", username, discriminator);
	}

	@Override
	public CompletableFuture<IMessage> sendEmbed(RichEmbed embed) {
		return null;
	}

	@Override
	public CompletableFuture<IMessage> sendFile(File file) {
		return null;
	}

	@Override
	public CompletableFuture<IMessage> sendFile(Resource resource) {
		return null;
	}

	@Override
	public CompletableFuture<IMessage> sendMessage(String content) {
		return null;
	}

	@Override
	public CompletableFuture<IMessage> sendMessage(String content, RichEmbed embed) {
		return null;
	}

	@Override
	public CompletableFuture<IMessage> sendMessage(String content, RichEmbed embed, File file) {
		Attachment attachment = null;
		if (embed != null) {
			if (embed.thumbnail != null && embed.thumbnail.file != null) {
				file = embed.thumbnail.file;
				embed.thumbnail.file = null;
				attachment = new Attachment(file.getName());
			} else if (embed.getImage() != null && embed.getImage().file != null) {
				file = embed.getImage().file;
				attachment = new Attachment(file.getName());
			}
		}

		return new SendMessage<IPrivateChannel>(getPrivateChannel(), content, embed, attachment, file).execute();
	}

	@Override
	public CompletableFuture<IMessage> sendMessage(String content, RichEmbed embed, Resource resource) {
		return null;
	}

}
