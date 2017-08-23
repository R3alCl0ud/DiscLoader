/**
 * 
 */
package io.discloader.discloader.common.event.guild;

import io.discloader.discloader.entity.guild.IGuild;

/**
 * @author Perry Berman
 */
public class GuildDeleteEvent extends GuildEvent {

	public GuildDeleteEvent(IGuild guild) {
		super(guild);
	}

}
