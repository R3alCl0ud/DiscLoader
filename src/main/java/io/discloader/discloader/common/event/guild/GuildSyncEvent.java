package io.discloader.discloader.common.event.guild;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.guild.Guild;

public class GuildSyncEvent extends DLEvent {

	private Guild guild;

	public GuildSyncEvent(Guild guild) {
		super(guild.loader);
		setGuild(guild);
	}

	/**
	 * @return the guild
	 */
	public Guild getGuild() {
		return guild;
	}

	/**
	 * @param guild the guild to set
	 */
	protected void setGuild(Guild guild) {
		this.guild = guild;
	}

}
