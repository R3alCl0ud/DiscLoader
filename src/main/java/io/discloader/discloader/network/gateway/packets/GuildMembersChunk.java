package io.discloader.discloader.network.gateway.packets;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.IEventListener;
import io.discloader.discloader.entity.guild.Guild;
import io.discloader.discloader.entity.guild.GuildMember;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.GuildMembersChunkJSON;
import io.discloader.discloader.network.json.MemberJSON;
import io.discloader.discloader.util.DLUtil;

import java.util.HashMap;

/**
 * @author Perry Berman
 *
 */
public class GuildMembersChunk extends DLPacket {

    public GuildMembersChunk(DiscSocket socket) {
        super(socket);
    }

    public void handle(SocketPacket packet) {
        String d = this.gson.toJson(packet.d);
        GuildMembersChunkJSON data = this.gson.fromJson(d, GuildMembersChunkJSON.class);
        Guild guild = this.loader.guilds.get(data.guild_id);
        HashMap<String, GuildMember> members = new HashMap<String, GuildMember>();
        for (MemberJSON m : data.members) {
            members.put(m.user.id, guild.addMember(m));
        }
        if (!this.loader.ready && this.socket.status != DLUtil.Status.READY) {
            this.loader.checkReady();
        } else if (this.loader.ready && this.socket.status == DLUtil.Status.READY) {
            for (IEventListener e : DiscLoader.handlers.values()) {
                e.GuildMembersChunk(members);
            }
        }
    }
}
