package io.discloader.discloader.network.json;

/**
 * @author perryberman
 *
 */
public class ReadyJSON {
	public int v;
	public UserJSON user;
	public String session_id;
	public String[] _trace;
	public GuildJSON[] guilds;
	public ChannelJSON[] private_channels;
}
