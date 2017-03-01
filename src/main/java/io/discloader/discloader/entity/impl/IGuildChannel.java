package io.discloader.discloader.entity.impl;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.GuildMember;
import io.discloader.discloader.entity.Overwrite;
import io.discloader.discloader.entity.Permission;

public interface IGuildChannel extends IChannel {

    CompletableFuture<? extends IGuildChannel> setName(String name);

    CompletableFuture<? extends IGuildChannel> setPosition(int position);

    CompletableFuture<? extends IGuildChannel> setPermissions(int allow, int deny, String type);

    HashMap<String, GuildMember> getMembers();

    Permission permissionsFor(GuildMember member);

    HashMap<String, Overwrite> overwritesOf(GuildMember member);

}
