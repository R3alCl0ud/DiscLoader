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
public class GuildUpdateEvent extends DLEvent {

	public final IGuild guild;
	
	public GuildUpdateEvent(IGuild guild2) {
		super(guild2.getLoader());
		this.guild = guild2;
	}

}
