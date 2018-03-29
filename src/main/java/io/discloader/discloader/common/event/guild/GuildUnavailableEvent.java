package io.discloader.discloader.common.event.guild;

import io.discloader.discloader.entity.guild.IGuild;

public class GuildUnavailableEvent extends GuildEvent {

	public GuildUnavailableEvent(IGuild guild) {
		super(guild);
	}

}
