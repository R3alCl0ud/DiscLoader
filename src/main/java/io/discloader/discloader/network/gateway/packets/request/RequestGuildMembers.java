package io.discloader.discloader.network.gateway.packets.request;

import io.discloader.discloader.core.entity.guild.Guild;

/**
 * @author Perry Berman
 *
 */
public class RequestGuildMembers {

	public final String guild_id;
	
	public final String query;
	
	public final int limit;
	
	public RequestGuildMembers(Guild guild, String query, int limit) {
		this.guild_id = Long.toUnsignedString(guild.getID());
		
		this.query = query;
		
		this.limit = limit;
	}

}
