/**
 * 
 */
package io.discloader.discloader.common.event.guild;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.core.entity.guild.Guild;

/**
 * @author Perry Berman
 *
 */
public class GuildDeleteEvent extends DLEvent {

	public final Guild guild;
	
	public GuildDeleteEvent(Guild guild) {
		super(guild.getLoader());
		this.guild = guild;
	}

}
