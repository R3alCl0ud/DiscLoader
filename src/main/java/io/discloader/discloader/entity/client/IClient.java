package io.discloader.discloader.entity.client;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.Shard;
import io.discloader.discloader.common.ShardManager;
import io.discloader.discloader.entity.channel.IChannel;
import io.discloader.discloader.entity.channel.IPrivateChannel;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.channel.IVoiceChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.user.IUser;

public interface IClient {

	CompletableFuture<Void> disconnect();

	CompletableFuture<Void> disconnect(String reason, int code);

	Map<Long, IChannel> getChannels();

	Map<Long, IGuild> getGuilds();

	Map<Long, IPrivateChannel> getPrivateChannels();

	Shard getShard();

	ShardManager getShardingManager();

	Map<Long, ITextChannel> getTextChannels();

	Map<Long, IUser> getUsers();

	Map<Long, IVoiceChannel> getVoiceChannels();

	boolean isSharded();

	CompletableFuture<IClient> login();

	CompletableFuture<IClient> login(String token);

}
