/**
 * 
 */
package io.discloader.discloader.common.event.guild;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.user.IUser;

/**
 * @author Perry Berman
 */
public class GuildBanAddEvent extends DLEvent {

	private final IUser user;

	private final IGuild guild;

	public GuildBanAddEvent(IGuild guild, IUser user) {
		super(guild.getLoader());

		this.user = user;

		this.guild = guild;
	}

	public IUser getBannedUser() {
		return user;
	}

	/**
	 * @return the guild
	 */
	public IGuild getGuild() {
		return guild;
	}

}
