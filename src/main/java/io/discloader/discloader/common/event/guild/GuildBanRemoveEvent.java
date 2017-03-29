/**
 * 
 */
package io.discloader.discloader.common.event.guild;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.guild.Guild;
import io.discloader.discloader.entity.user.User;

/**
 * @author Perry Berman
 *
 */
public class GuildBanRemoveEvent extends DLEvent {

	public final Guild guild;
	public final User user;
	

	/**
	 * Creates a new GuildBanRemoveEvent Object
	 * @param guild The guild the user was unbanned from
	 * @param user The use that has been unbanned
	 */
	public GuildBanRemoveEvent(Guild guild, User user) {
		super(guild.getLoader());
		this.guild = guild;
		this.user = user;
	}

}
