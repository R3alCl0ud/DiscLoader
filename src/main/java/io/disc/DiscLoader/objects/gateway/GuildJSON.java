package io.disc.DiscLoader.objects.gateway;

/**
 * @author perryberman
 *
 */
public class GuildJSON {
	public String id;
	public String name;
	public String owner_id;
	public boolean unavailable;
	
	public MemberJSON[] members;
	public ChannelJSON[] channels;
	
	public PresenceJSON[] presences;
}
