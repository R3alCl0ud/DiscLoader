package io.discloader.discloader.network.gateway.packets.request;

import io.discloader.discloader.entity.Guild;

/**
 * @author Perry Berman
 *
 */
public class RequestGuildMembers {

	public final String guild_id;
	
	public final String query;
	
	public final int limit;
	
	public RequestGuildMembers(Guild guild, String query, int limit) {
		this.guild_id = guild.id;
		
		this.query = query;
		
		this.limit = limit;
	}

}
