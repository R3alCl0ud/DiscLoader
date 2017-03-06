package io.discloader.discloader.entity.channels;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.Guild;
import io.discloader.discloader.entity.GuildMember;
import io.discloader.discloader.entity.Overwrite;
import io.discloader.discloader.entity.Permission;
import io.discloader.discloader.entity.Role;
import io.discloader.discloader.entity.impl.IGuildChannel;
import io.discloader.discloader.network.json.ChannelJSON;
import io.discloader.discloader.util.DLUtil;

public class GuildChannel extends Channel implements IGuildChannel {
    /**
     * The {@link Guild} the channel belongs to. <br>
     * This property <u>must</u> be {@code null} if the {@link #type} of the channel is {@code "dm"}, or {@code "groupDM"}.
     * 
     * @author Perry Berman
     * @since 0.0.1
     */
    public final Guild guild;

    /**
     * The channel's name
     */
    public String name;
    
    public int position;

    /**
     * A {@link HashMap} of the channel's {@link Overwrite overwrites}. Indexed by {@link Overwrite#id}.
     * 
     * @author Perry Berman
     * @since 0.0.1
     */
    public HashMap<String, Overwrite> overwrites;

    /**
     * A {@link HashMap} of the channel's {@link GuildMember members}. Indexed by {@link GuildMember #id member.id}. <br>
     * Is {@code null} if {@link #guild} is {@code null}, and if {@link #type} is {@code "dm"}, or {@code "groupDM"}.
     * 
     * @author Perry Berman
     * @since 0.0.1
     */
    HashMap<String, GuildMember> members = new HashMap<String, GuildMember>();

    public GuildChannel(Guild guild, ChannelJSON channel) {
        super(guild.loader, channel);

        this.guild = guild;

        this.overwrites = new HashMap<String, Overwrite>();
    }

    public HashMap<String, GuildMember> getMembers() {
        HashMap<String, GuildMember> members = new HashMap<String, GuildMember>();
        for (GuildMember member : this.guild.members.values()) {
            if (this.permissionsFor(member).hasPermission(DLUtil.PermissionFlags.READ_MESSAGES, false))
                members.put(member.id, member);
        }
        return members;
    }

    public HashMap<String, Overwrite> overwritesOf(GuildMember member) {
        HashMap<String, Overwrite> Overwrites = new HashMap<String, Overwrite>();
        for (Role role : member.getRoleList().values()) {
            if (this.overwrites.get(role.id) != null)
                Overwrites.put(role.id, this.overwrites.get(role.id));
        }
        if (this.overwrites.get(member.id) != null)
            Overwrites.put(member.id, this.overwrites.get(member.id));
        return Overwrites;
    }

    public Permission permissionsFor(GuildMember member) {
        int raw = 0;
        if (member.id == this.guild.ownerID)
            return new Permission(member, this, 2146958463);
        for (Role role : member.getRoleList().values())
            raw |= role.permissions;
        for (Overwrite overwrite : this.overwritesOf(member).values()) {
            raw |= overwrite.allow;
            raw &= ~overwrite.deny;
        }
        return new Permission(member, this, raw);
    }

    @Override
    public CompletableFuture<? extends IGuildChannel> setName(String name) {
        return null;
    }

    @Override
    public CompletableFuture<? extends IGuildChannel> setPermissions(int allow, int deny, String type) {
        return null;
    }

    @Override
    public CompletableFuture<? extends IGuildChannel> setPosition(int position) {
        return null;
    }

    public boolean isPrivate() {
        return false;
    }

    public void setup(ChannelJSON data) {
        super.setup(data);

        this.name = data.name;

        this.position = data.position;
    }

}
