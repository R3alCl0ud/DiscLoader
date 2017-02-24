/**
 * 
 */
package io.discloader.discloader.common.event;

import io.discloader.discloader.entity.Guild;

/**
 * @author Perry Berman
 *
 */
public class GuildUpdateEvent extends DLEvent {

	public final Guild guild;
	
	public GuildUpdateEvent(Guild guild) {
		super(guild.loader);
		this.guild = guild;
	}

}
