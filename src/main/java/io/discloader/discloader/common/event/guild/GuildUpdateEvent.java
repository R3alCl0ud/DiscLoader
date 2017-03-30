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
public class GuildUpdateEvent extends DLEvent {

	public final Guild guild;
	
	public GuildUpdateEvent(Guild guild) {
		super(guild.getLoader());
		this.guild = guild;
	}

}
