/**
 * 
 */
package io.discloader.discloader.common.event.guild;

import io.discloader.discloader.entity.guild.IGuild;

/**
 * @author Perry Berman
 *
 */
public class GuildUpdateEvent extends GuildEvent {

	private final IGuild oldGuild;
	
	public GuildUpdateEvent(IGuild guild, IGuild oldGuild) {
		super(guild);
		this.oldGuild = oldGuild;
	}

	/**
	 * @return the oldGuild
	 */
	public IGuild getOldGuild() {
		return oldGuild;
	}

}
