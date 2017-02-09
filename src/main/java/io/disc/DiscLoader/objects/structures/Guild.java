package io.disc.DiscLoader.objects.structures;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.objects.gateway.ChannelJSON;
import io.disc.DiscLoader.objects.gateway.GuildJSON;
import io.disc.DiscLoader.objects.gateway.MemberJSON;
import io.disc.DiscLoader.objects.gateway.PresenceJSON;
import io.disc.DiscLoader.objects.gateway.RoleJSON;
import io.disc.DiscLoader.util.constants;

public class Guild {
	public final String id;
	public String name;
	public String ownerID;
	public String icon;
	public boolean available;

	public GuildMember owner;

	public final DiscLoader loader;

	public HashMap<String, GuildMember> members;

	public HashMap<String, TextChannel> textChannels;
	
	public HashMap<String, VoiceChannel> voiceChannels; 

	public HashMap<String, Role> roles;

	public HashMap<String, Presence> presences;

	public Guild(DiscLoader loader, GuildJSON data) {
		this.loader = loader;

		this.members = new HashMap<String, GuildMember>();
		this.textChannels = new HashMap<String, TextChannel>();
		this.voiceChannels = new HashMap<String, VoiceChannel>();
		this.roles = new HashMap<String, Role>();
		this.presences = new HashMap<String, Presence>();

		if (data.unavailable == true) {
			this.available = false;
			this.id = data.id;
		} else {
			this.available = true;
			this.id = data.id;
			this.setup(data);
		}
	}

	public void setup(GuildJSON data) {

		this.name = data.name;
		this.icon = data.icon != null ? data.icon : null;
		this.ownerID = data.owner_id;
		if (data.roles.length > 0) {
			this.roles.clear();
			for (RoleJSON role : data.roles) {
				this.addRole(role);
			}
		}
		if (data.members != null && data.members.length > 0) {
			this.members.clear();
			for (MemberJSON member : data.members) {
				this.addMember(member);
			}
		}
		if (data.channels != null && data.channels.length > 0) {
			this.textChannels.clear();
			this.voiceChannels.clear();
			for (ChannelJSON channel : data.channels) {
				this.loader.addChannel(channel, this);
			}
		}
		if (data.presences != null && data.presences.length > 0) {
			this.presences.clear();
			for (PresenceJSON presence : data.presences) {
				this.setPresence(presence);
			}
		}
		this.available = !data.unavailable;
	}

	public GuildMember addMember(MemberJSON data) {
		boolean exists = this.members.containsKey(data.user.id);
		GuildMember member = new GuildMember(this, data);
		this.members.put(member.id, member);
		if (member.id.equals(this.ownerID)) this.owner = member;
		if (!exists && this.loader.ready) {
			this.loader.emit(constants.Events.GUILD_MEMBER_ADD, member);
		}
		return member;
	}

	public GuildMember addMember(User user, String[] roles, boolean deaf, boolean mute, String nick, boolean emitEvent) {
		boolean exists = this.members.containsKey(user.id);
		GuildMember member = new GuildMember(this, user, roles, deaf, mute, nick);
		this.members.put(member.id, member);
		if (member.id.equals(this.ownerID)) this.owner = member;
		if (this.loader.ready == true && emitEvent && !exists) {
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

	public CompletableFuture<Guild> delete() {
		return null;
	}

	/**
	 * @param name
	 * @return
	 */
	public CompletableFuture<Guild> setName(String name) {
		return this.loader.rest.modifyGuild(this, new JSONObject().put("name", name));
	}
	
	/**
	 * @param icon
	 * @return
	 * @throws IOException
	 */
	public CompletableFuture<Guild> setIcon(String icon) throws IOException {
		String base64 = new String("data:image/jpg;base64," + Base64.encodeBase64String(Files.readAllBytes(Paths.get(icon))));
		return this.loader.rest.modifyGuild(this, new JSONObject().put("icon", base64));
	}
	
	/**
	 * @param region
	 * @return
	 */
	public CompletableFuture<Guild> setVoiceRegion(String region) {
		return this.loader.rest.modifyGuild(this, new JSONObject().put("region", region));
	}
	
	/**
	 * @param memberID
	 * @return
	 */
	public CompletableFuture<GuildMember> loadMember(String memberID) {
		return this.loader.rest.loadGuildMember(this, memberID);
	}
	
	/**
	 * @param limit
	 * @param before
	 * @return
	 */
	public CompletableFuture<HashMap<String, GuildMember>> loadMembers(int limit, String before) {
		return null;
	}
	
}