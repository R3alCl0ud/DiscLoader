package io.discloader.discloader.entity.impl;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.Guild;
import io.discloader.discloader.entity.GuildMember;
import io.discloader.discloader.entity.Overwrite;
import io.discloader.discloader.entity.Permission;

public interface IGuildChannel extends IChannel {

    /**
     * Gets the members that can view/join this channel
     * 
     * @return A HashMap of GuildMember with access to view the channel
     */
    HashMap<String, GuildMember> getMembers();

    /**
     * Gets all of the channel's {@link Overwrite overwrites} that applies to a {@link GuildMember}
     * 
     * @param member The member of whome we are looking for overwrites that apply.
     * @author Perry Berman
     * @return A {@link HashMap} of overwrite objects, indexed by {@link Overwrite#id}
     * @since 0.0.1
     */
    HashMap<String, Overwrite> overwritesOf(GuildMember member);

    /**
     * Evaluates the permissions for a member in the channel's {@link Guild}
     * 
     * @param member The member whose permissions we are evaluating.
     * @return A new Permissions object that contains {@literal this}, the {@literal member}, and their evaluated permissions
     *         {@link Integer}. <br>
     *         null if the channel doesn't belong to a {@link Guild}
     */
    Permission permissionsFor(GuildMember member);

    CompletableFuture<? extends IGuildChannel> setName(String name);

    CompletableFuture<? extends IGuildChannel> setPermissions(int allow, int deny, String type);

    CompletableFuture<? extends IGuildChannel> setPosition(int position);

}
