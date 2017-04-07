package io.discloader.discloader.entity.user;

import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.client.render.util.IIcon;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.util.ICreationTime;
import io.discloader.discloader.entity.util.ISnowflake;
import io.discloader.discloader.network.json.UserJSON;

/**
 * @author Perry Berman
 */
public interface IUser extends ISnowflake, ICreationTime {

	String asMention();

	IIcon getAvatar();

	String getDiscriminator();

	DiscLoader getLoader();

	CompletableFuture<IUserProfile> getProfile();

	String getUsername();

	boolean isBot();

	boolean isVerified();

	boolean MFAEnabled();

	void setup(UserJSON data);

	String toString();

}
