package io.disc.DiscLoader.objects.gateway;

/**
 * @author perryberman
 *
 */
public class GuildGateway {
	public String id;
	public String name;
	public String owner_id;
	public boolean unavailable;
	
	public Member[] members;
	
	public PresenceGate[] presences;
}
