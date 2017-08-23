package io.discloader.discloader.common.event.guild;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.guild.IGuild;

public class GuildEvent extends DLEvent {

	private final IGuild guild;

	public GuildEvent(IGuild guild) {
		super(guild.getLoader());
		this.guild = guild;
	}

	/**
	 * @return the guild
	 */
	public IGuild getGuild() {
		return guild;
	}

}
