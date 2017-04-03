package io.discloader.discloader.entity;

import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.guild.IRole;

public interface IOverwrite extends ISnowflake {

	/**
	 * @return
	 */
	int getAllowed();

	/**
	 * @return
	 */
	int getDenied();

	IGuildMember getMember();

	IRole getRole();

	String getType();

}
