package io.discloader.discloader.entity.invite;

import io.discloader.discloader.entity.util.ICreationTime;
import io.discloader.discloader.entity.util.ISnowflake;
import io.discloader.discloader.util.DLUtil.ChannelType;

public interface IInviteChannel extends ISnowflake, ICreationTime {

	String getName();

	ChannelType getType();

}
