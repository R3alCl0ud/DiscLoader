package io.disc.DiscLoader.objects.gateway;

/**
 * @author perryberman
 *
 */
public class Ready {
	public int v;
	public UserGateway user;
	public String session_id;
	public String[] _trace;
	public GuildGateway[] guilds;
	public DMChannelGateway[] private_channels;
}
