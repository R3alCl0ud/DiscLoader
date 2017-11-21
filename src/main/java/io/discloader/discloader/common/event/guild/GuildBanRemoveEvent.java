/**
 * 
 */
package io.discloader.discloader.common.event.guild;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.user.IUser;

/**
 * @author Perry Berman
 *
 */
public class GuildBanRemoveEvent extends DLEvent {
	
	public final IGuild guild;
	public final IUser user;
	
	/**
	 * Creates a new GuildBanRemoveEvent Object
	 * 
	 * @param guild The guild the user was unbanned from
	 * @param user The use that has been unbanned
	 */
	public GuildBanRemoveEvent(IGuild guild, IUser user) {
		super(guild.getLoader());
		this.guild = guild;
		this.user = user;
	}
	
}
