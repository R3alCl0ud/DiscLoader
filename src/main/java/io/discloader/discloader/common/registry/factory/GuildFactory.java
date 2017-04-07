package io.discloader.discloader.common.registry.factory;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.core.entity.guild.Guild;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.network.json.GuildJSON;

public class GuildFactory {

	public IGuild buildGuild(GuildJSON data) {
		return new Guild(DiscLoader.getDiscLoader(), data);
	}

}
