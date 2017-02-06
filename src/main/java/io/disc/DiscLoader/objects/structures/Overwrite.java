/**
 * 
 */
package io.disc.DiscLoader.objects.structures;

import io.disc.DiscLoader.objects.gateway.OverwriteJSON;

/**
 * @author Perry Berman
 *
 */
public class Overwrite {

	public int allow;
	public int deny;
	public String type;
	public String id;
	public Role role;
	public GuildMember member;
	
	public Overwrite(OverwriteJSON data) {
		this.allow = data.allow;
		this.deny = data.deny;
		this.id = data.id;
		this.type = data.type;
	}

	public Overwrite(Overwrite data, GuildMember member) {
		this.allow = data.allow;
		this.deny = data.deny;
		this.type = data.type;
		this.member = member;
	}
	
	public Overwrite(Overwrite data, Role role) {
		this.allow = data.allow;
		this.deny = data.deny;
		this.type = data.type;
		this.role = role;
	}
	
	
	
}
