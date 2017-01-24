package io.disc.DiscLoader.objects.gateway;

/**
 * @author perryberman
 *
 */
public class Ready {
	public int v;
	public User user;
	String session_id;
	String[] _trace;
	Guild[] guilds;
	Channel[] private_channels;
}
