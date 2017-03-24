package io.discloader.discloader.network.rest.actions;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.channels.impl.VoiceChannel;
import io.discloader.discloader.entity.guild.GuildMember;
import io.discloader.discloader.entity.guild.Role;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

public class ModifyMember {

	public String nick;
	public String channel_id;

	public boolean mute;
	public boolean deaf;

	public String[] roles;

	private DiscLoader loader;
	private GuildMember member;

	public ModifyMember(GuildMember member, VoiceChannel channel) {
		this.channel_id = channel.getID();
		this.member = member;
		this.nick = member.getNickname();
		this.roles = new String[member.getRoles().size()];
		int i = 0;
		for (Role r : member.getRoles().values()) {
			roles[i] = r.id;
			i++;
		}
		mute = member.isMuted();
		deaf = member.isDeaf();
	}

	public ModifyMember(GuildMember member, String nick, HashMap<String, Role> roles, boolean mute, boolean deaf, VoiceChannel channel) {
		this.channel_id = channel.getID();
		this.nick = nick;
		this.mute = mute;
		this.deaf = deaf;
		this.loader = channel.loader;
		this.member = member;
		this.roles = new String[roles.size()];
		int i = 0;
		for (Role r : roles.values()) {
			this.roles[i] = r.id;
			i++;
		}
	}

	public CompletableFuture<GuildMember> execute() {
		CompletableFuture<GuildMember> future = new CompletableFuture<>();
		loader.rest.makeRequest(Endpoints.guildMember(member.guild.id, member.id), Methods.PATCH, true, this).thenAcceptAsync(action -> {
			future.complete(member);
		});
		return future;
	}

}
