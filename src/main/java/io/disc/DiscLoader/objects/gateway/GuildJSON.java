package io.disc.DiscLoader.objects.gateway;

/**
 * @author Perry Berman
 *
 */
public class GuildJSON {
	public String id;
	public String name;
	public String icon;
	public String splash;
	public String owner_id;
	public String embed_channel_id;
	public String[] features;
	
	public int verification_level;
	public int default_message_notifications;
	
	public boolean large;
	public boolean embed_enabled;
	public boolean unavailable;
	
	public MemberJSON[] members;
	public ChannelJSON[] channels;
	public RoleJSON[] roles;
	public PresenceJSON[] presences;
	
}
