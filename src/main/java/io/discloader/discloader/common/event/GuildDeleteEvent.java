/**
 * 
 */
package io.discloader.discloader.common.event;

import io.discloader.discloader.entity.Guild;

/**
 * @author Perry Berman
 *
 */
public class GuildDeleteEvent extends DiscEvent {

	public final Guild guild;
	
	public GuildDeleteEvent(Guild guild) {
		super(guild.loader);
		this.guild = guild;
	}

}
