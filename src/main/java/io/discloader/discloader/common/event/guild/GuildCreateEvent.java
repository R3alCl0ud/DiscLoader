/**
 * 
 */
package io.discloader.discloader.common.event.guild;

import io.discloader.discloader.common.entity.guild.Guild;
import io.discloader.discloader.common.event.DLEvent;

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
