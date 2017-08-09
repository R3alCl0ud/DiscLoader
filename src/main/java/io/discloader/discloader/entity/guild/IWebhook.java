package io.discloader.discloader.entity.guild;

import io.discloader.discloader.entity.IIcon;
import io.discloader.discloader.entity.channel.IChannel;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.ISnowflake;

public interface IWebhook extends ISnowflake {

	IGuild getGuild();

	IChannel getChannel();

	IUser getUser();

	String getName();

	IIcon getAvatar();

	String getToken();

}
