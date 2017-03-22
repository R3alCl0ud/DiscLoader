package io.discloader.discloader.network.gateway.packets;

import com.google.gson.Gson;

import io.discloader.discloader.common.event.guild.GuildCreateEvent;
import io.discloader.discloader.entity.guild.Guild;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.network.json.GuildJSON;
import io.discloader.discloader.util.DLUtil;

public class GuildCreate extends DLPacket {

    public GuildCreate(DiscSocket socket) {
        super(socket);
    }

    @Override
    public void handle(SocketPacket packet) {
        Gson gson = new Gson();
        String d = gson.toJson(packet.d);
        GuildJSON data = gson.fromJson(d, GuildJSON.class);
        Guild guild = null;
        if (this.socket.loader.guilds.containsKey(data.id))
            guild = this.socket.loader.guilds.get(data.id);
        if (guild != null) {
            if (!guild.available && !data.unavailable) {
                guild.setup(data);
                this.socket.loader.checkReady();
                if (this.socket.status == DLUtil.Status.READY && this.socket.loader.ready) {
                    this.socket.loader.emit(DLUtil.Events.GUILD_CREATE, new GuildCreateEvent(guild));
                }
            }
        } else {
            // a brand new guild
            guild = this.socket.loader.addGuild(data);
            
        }
    }

}
