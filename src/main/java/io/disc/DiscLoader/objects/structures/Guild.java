package io.disc.DiscLoader.objects.structures;

import java.util.HashMap;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.objects.gateway.ChannelJSON;
import io.disc.DiscLoader.objects.gateway.GuildJSON;
import io.disc.DiscLoader.objects.gateway.MemberJSON;
import io.disc.DiscLoader.objects.gateway.PresenceJSON;
import io.disc.DiscLoader.objects.gateway.RoleJSON;
import io.disc.DiscLoader.util.constants;

public class Guild {
	public String id;
	public String name;
	public String ownerID;
	public String icon;
	public boolean available;
	
	public final DiscLoader loader;
	
	public HashMap<String, GuildMember> members;
	
	public HashMap<String, Channel> channels;
	
	public HashMap<String, Role> roles;
	
	public HashMap<String, Presence> presences;
	
	public Guild(DiscLoader loader, GuildJSON guild) {
		this.loader = loader;
		
		this.members = new HashMap<String, GuildMember>();
		this.channels = new HashMap<String, Channel>();
		this.roles = new HashMap<String, Role>();
		this.presences = new HashMap<String, Presence>();
		
		if (guild.unavailable == true) {
			this.available = false;
			this.id = guild.id;
		} else {
			this.available = true;
			this.setup(guild);
		}	
	}
	
	public void setup(GuildJSON data) {
		this.id = data.id;
		this.name = data.name;
		this.icon = data.icon != null ? data.icon : null;
		if (data.roles.length > 0) {
			this.roles.clear();
			for (RoleJSON role : data.roles) {
				this.addRole(role);
			}
		}
		if (data.members != null) {
			this.members.clear();
			for (MemberJSON member : data.members) {
				this.addMember(member);
			}
		}
		
		if (data.channels != null) {
			this.channels.clear();
			for (ChannelJSON channel : data.channels) {
				this.loader.addChannel(channel, this);
			}
		}
		this.available = !data.unavailable;
	}
	
	public GuildMember addMember(MemberJSON guildUser) {
		boolean exists = this.members.containsKey(guildUser.user.id);
		GuildMember member = new GuildMember(this, guildUser);
		this.members.put(member.user.id, member);
		if (!exists && this.loader.ready) {
			this.loader.emit(constants.Events.GUILD_MEMBER_ADD, member);
		}
		return member;
	}
	
	public Role addRole(RoleJSON guildRole) {
		boolean exists = this.roles.containsKey(guildRole.id);
		Role role = new Role(this, guildRole);
		this.roles.put(role.id, role);
		if (!exists && this.loader.ready) {
			this.loader.emit(constants.Events.GUILD_ROLE_CREATE, role);
		}
		return role;
	}
	
	public void setPresence(PresenceJSON guildPresence) {
		if (this.presences.containsKey(guildPresence.user.id)) {
			this.presences.get(guildPresence.user.id).update(guildPresence);
			return;
		}
		Presence presence = new Presence(guildPresence);
		this.presences.put(guildPresence.user.id, presence);
	}
	
}