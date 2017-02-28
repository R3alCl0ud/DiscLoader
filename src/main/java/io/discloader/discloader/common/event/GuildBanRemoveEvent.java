/**
 * 
 */
package io.discloader.discloader.common.event;

import io.discloader.discloader.entity.Guild;
import io.discloader.discloader.entity.User;

/**
 * @author Perry Berman
 *
 */
public class GuildBanRemoveEvent extends DLEvent {

	public final Guild guild;
	public final User user;
	
	/**
	 * @param loader
	 */
	public GuildBanRemoveEvent(Guild guild, User user) {
		super(guild.loader);
		this.guild = guild;
		this.user = user;
	}

}
