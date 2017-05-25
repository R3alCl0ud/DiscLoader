package io.discloader.discloader.entity;

import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.guild.IRole;
import io.discloader.discloader.entity.util.ISnowflake;
import io.discloader.discloader.entity.util.Permissions;

public interface IOverwrite extends ISnowflake {

	long getAllowed();

	long getDenied();

	IGuildMember getMember();

	IRole getRole();

	String getType();

	void setAllowed(Permissions... permissions);

	void setDenied(Permissions... permissions);
}
