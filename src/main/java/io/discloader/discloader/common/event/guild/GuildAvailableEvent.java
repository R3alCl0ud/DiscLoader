package io.discloader.discloader.common.event.guild;

import io.discloader.discloader.entity.guild.IGuild;

public class GuildAvailableEvent extends GuildEvent {

	public GuildAvailableEvent(IGuild guild) {
		super(guild);
	}

}
