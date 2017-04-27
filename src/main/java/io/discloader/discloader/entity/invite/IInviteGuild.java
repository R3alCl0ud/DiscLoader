package io.discloader.discloader.entity.invite;

import io.discloader.discloader.entity.IIcon;
import io.discloader.discloader.entity.util.ICreationTime;
import io.discloader.discloader.entity.util.ISnowflake;

public interface IInviteGuild extends ISnowflake, ICreationTime {

	IIcon getIcon();

	IIcon getSplash();

	String getName();

}
