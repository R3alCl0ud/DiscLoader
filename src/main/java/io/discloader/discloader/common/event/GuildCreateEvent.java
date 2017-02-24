/**
 * 
 */
package io.discloader.discloader.common.event;

import io.discloader.discloader.entity.Guild;

/**
 * @author Perry Berman
 *
 */
public class GuildCreateEvent extends DLEvent {

	public final Guild guild;
	
	public GuildCreateEvent(Guild guild) {
		super(guild.loader);
		this.guild = guild;
	}

}
