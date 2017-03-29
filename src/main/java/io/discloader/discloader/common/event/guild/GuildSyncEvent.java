package io.discloader.discloader.common.event.guild;

import io.discloader.discloader.common.entity.guild.Guild;
import io.discloader.discloader.common.event.DLEvent;

public class GuildSyncEvent extends DLEvent {

	private Guild guild;

	public GuildSyncEvent(Guild guild) {
		super(guild.getLoader());
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
