package io.disc.DiscLoader.objects.gateway;

/**
 * @author perryberman
 *
 */
public class Ready {
	public int v;
	public User user;
	public String session_id;
	public String[] _trace;
	public Guild[] guilds;
	public DMChannel[] private_channels;
}
