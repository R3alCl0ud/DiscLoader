package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.common.event.guild.member.GuildMemberUpdateEvent;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.core.entity.guild.GuildMember;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.MemberJSON;
import io.discloader.discloader.util.DLUtil.Status;

/**
 * @author Perry Berman
 */
public class GuildMemberUpdate extends AbstractHandler {

	public GuildMemberUpdate(DiscSocket socket) {
		super(socket);
	}

	@Override
	public void handle(SocketPacket packet) {
		String d = this.gson.toJson(packet);
		MemberJSON data = this.gson.fromJson(d, MemberJSON.class);
		IGuild guild = EntityRegistry.getGuildByID(data.guild_id);
		IGuildMember member = guild.getMember(SnowflakeUtil.parse(data.user.id));
		if (member == null) {
			member = guild.addMember(data);
		}
		GuildMember oldMember = new GuildMember(member);
		if (this.loader.ready && this.socket.status == Status.READY) {
			GuildMemberUpdateEvent event = new GuildMemberUpdateEvent(member, oldMember, guild);
			loader.emit(event);
		}
	}
}
