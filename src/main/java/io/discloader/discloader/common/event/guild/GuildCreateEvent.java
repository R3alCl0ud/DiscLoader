/**
 * 
 */
package io.discloader.discloader.common.event.guild;

import io.discloader.discloader.entity.guild.IGuild;

/**
 * @author Perry Berman
 */
public class GuildCreateEvent extends GuildEvent {

	public GuildCreateEvent(IGuild newGuild) {
		super(newGuild);
	}

}
