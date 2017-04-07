package io.discloader.discloader.entity;

import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.user.IUser;

/**
 * @author Perry Berman
 */
public interface IInvite extends ISnowflake {

	IGuildChannel getChannel();

	IGuild getGuild();

	IUser getInviter();

	int getMaxAge();

	int getMaxUses();

	int getUses();

	boolean isTemporary();

	boolean isValid();

}
