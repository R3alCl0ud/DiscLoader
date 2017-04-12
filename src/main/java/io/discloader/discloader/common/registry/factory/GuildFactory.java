package io.discloader.discloader.common.registry.factory;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.core.entity.guild.Guild;
import io.discloader.discloader.core.entity.guild.GuildMember;
import io.discloader.discloader.core.entity.guild.Role;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.guild.IRole;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.json.GuildJSON;
import io.discloader.discloader.network.json.MemberJSON;
import io.discloader.discloader.network.json.RoleJSON;

public class GuildFactory {

	public IGuild buildGuild(GuildJSON data) {
		return new Guild(DiscLoader.getDiscLoader(), data);
	}

	public IRole buildRole(IGuild guild, RoleJSON data) {
		return new Role(guild, data);
	}

	public IGuildMember buildMember(IGuild guild, MemberJSON data) {
		return new GuildMember(guild, data);
	}

	public IGuildMember buildMember(IGuild guild, IUser user) {
		return new GuildMember(guild, user);
	}

	public IGuildMember buildMember(IGuildMember member) {
		return new GuildMember(member);
	}

	public IGuildMember buildMember(IGuild guild, IUser user, String[] roles, boolean deaf, boolean mute, String nick) {
		return new GuildMember(guild, user, roles, deaf, mute, nick);
	}
}
