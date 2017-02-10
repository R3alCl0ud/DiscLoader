package io.disc.discloader.objects.structures;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.disc.discloader.DiscLoader;
import io.disc.discloader.objects.gateway.MemberJSON;

public class GuildMember {

	public final DiscLoader loader;
	public String nick;
	public final String id;
	public final User user;
	public final Guild guild;
	private String[] roleIDs;
	public boolean mute;
	public boolean deaf;

	public Presence frozenPresence;

	public Date joinedAt;

	public GuildMember(Guild guild, MemberJSON data) {
		this.loader = guild.loader;
		this.user = this.loader.addUser(data.user);
		this.id = this.user.id;
		this.guild = guild;
		this.nick = data.nick != null ? data.nick : this.user.username;
		// this.joinedAt = Date.from(Instant.parse(data.joined_at));
		this.roleIDs = data.roles;

		this.deaf = data.deaf;
		this.mute = this.deaf ? true : data.mute;

	}

	public GuildMember(Guild guild, User user, String[] roles, boolean deaf, boolean mute, String nick) {
		this.id = user.id;
		this.user = user;
		this.guild = guild;
		this.loader = guild.loader;
		this.nick = nick != null ? nick : this.user.username;
		this.roleIDs = roles;

		this.deaf = deaf;
		this.mute = this.deaf == true ? true : mute;
	}

	public GuildMember(GuildMember data) {
		this.id = data.id;
		this.loader = data.loader;
		this.user = data.user;
		this.guild = data.guild;
		this.nick = data.nick;
		this.roleIDs = data.roleIDs;

		this.deaf = data.deaf;
		this.mute = this.deaf ? true : data.mute;
	}

	public String toString() {
		return this.user.toString();
	}

	public Presence getPresence() {
		return this.guild.presences.get(this.user.id);
	}

	public HashMap<String, Role> getRoleList() {
		HashMap<String, Role> roles = new HashMap<String, Role>();
		for (String id : this.roleIDs) {
			roles.put(id, this.guild.roles.get(id));
		}
		return roles;
	}

	public CompletableFuture<GuildMember> setNick(String nick) {
		return this.loader.rest.setNick(this, nick);
	}

	public CompletableFuture<GuildMember> ban() {
		return null;
	}

	public CompletableFuture<GuildMember> kick() {
		return null;
	}
}
