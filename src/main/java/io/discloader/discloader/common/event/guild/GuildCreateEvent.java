/**
 * 
 */
package io.discloader.discloader.common.event.guild;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.guild.Guild;

/**
 * @author Perry Berman
 *
 */
public class GuildCreateEvent extends DLEvent {

	public final Guild guild;
	
	public GuildCreateEvent(Guild guild) {
		super(guild.getLoader());
		this.guild = guild;
	}

}
