/**
 * 
 */
package io.discloader.discloader.common.event.guild;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.guild.IGuild;

/**
 * @author Perry Berman
 *
 */
public class GuildCreateEvent extends DLEvent {

	public final IGuild guild;
	
	public GuildCreateEvent(IGuild newGuild) {
		super(newGuild.getLoader());
		this.guild = newGuild;
	}

}
