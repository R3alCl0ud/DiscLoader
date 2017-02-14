package io.discloader.discloader.objects.gateway;

/**
 * @author perryberman
 *
 */
public class Ready {
	public int v;
	public UserJSON user;
	public String session_id;
	public String[] _trace;
	public GuildJSON[] guilds;
	public ChannelJSON[] private_channels;
}
