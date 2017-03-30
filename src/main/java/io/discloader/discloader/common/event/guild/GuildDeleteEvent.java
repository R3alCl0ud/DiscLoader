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
public class GuildDeleteEvent extends DLEvent {
	
	public final IGuild guild;
	
	public GuildDeleteEvent(IGuild guild) {
		super(guild.getLoader());
		this.guild = guild;
	}
	
}
