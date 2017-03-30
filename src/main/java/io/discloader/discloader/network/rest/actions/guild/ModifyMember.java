package io.discloader.discloader.network.rest.actions.guild;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.channel.IGuildVoiceChannel;
import io.discloader.discloader.entity.channel.IVoiceChannel;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.guild.IRole;
import io.discloader.discloader.util.DLUtil.Endpoints;
import io.discloader.discloader.util.DLUtil.Methods;

public class ModifyMember {

	public String nick;
	public String channel_id;

	public boolean mute;
	public boolean deaf;

	public String[] roles;

	private DiscLoader loader;
	private IGuildMember member;

	public ModifyMember(IGuildMember member, IGuildVoiceChannel channel) {
		this.channel_id = channel.getID();
		this.member = member;
		this.nick = member.getNickname();
		this.roles = new String[member.getRoles().size()];
		int i = 0;
		for (IRole r : member.getRoles().values()) {
			roles[i] = r.getID();
			i++;
		}
		mute = member.isMuted();
		deaf = member.isDeaf();
	}

	public ModifyMember(IGuildMember member, String nick, HashMap<String, IRole> hashMap, boolean mute, boolean deaf, IVoiceChannel channel) {
		this.channel_id = channel.getID();
		this.nick = nick;
		this.mute = mute;
		this.deaf = deaf;
		loader = channel.getLoader();
		this.member = member;
		this.roles = new String[hashMap.size()];
		int i = 0;
		for (IRole r : hashMap.values()) {
			this.roles[i] = r.getID();
			i++;
		}
	}

	public CompletableFuture<IGuildMember> execute() {
		CompletableFuture<IGuildMember> future = new CompletableFuture<>();
		loader.rest.makeRequest(Endpoints.guildMember(member.getGuild().getID(), member.getID()), Methods.PATCH, true, this).thenAcceptAsync(action -> {
			future.complete(member);
		});
		return future;
	}

}
