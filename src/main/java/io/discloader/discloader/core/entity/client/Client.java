/**
 * Client Implementation package
 */
package io.discloader.discloader.core.entity.client;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.Shard;
import io.discloader.discloader.common.ShardManager;
import io.discloader.discloader.entity.channel.IChannel;
import io.discloader.discloader.entity.channel.IPrivateChannel;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.channel.IVoiceChannel;
import io.discloader.discloader.entity.client.IClient;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.user.IUser;

/**
 * @author perryberman
 *
 */
public class Client implements IClient {

	/**
	 * @return
	 */
	@Override
	public Map<Long, IChannel> getChannels() {
		return null;
	}

	/**
	 * @return
	 */
	@Override
	public Map<Long, IGuild> getGuilds() {
		return null;
	}

	/**
	 * @return
	 */
	@Override
	public Map<Long, IPrivateChannel> getPrivateChannels() {
		return null;
	}

	/**
	 * @return
	 */
	@Override
	public Shard getShard() {
		return null;
	}

	/**
	 * @return
	 */
	@Override
	public ShardManager getShardingManager() {
		return null;
	}

	/**
	 * @return
	 */
	@Override
	public Map<Long, ITextChannel> getTextChannels() {
		return null;
	}

	/**
	 * @return
	 */
	@Override
	public Map<Long, IUser> getUsers() {
		return null;
	}

	/**
	 * @return
	 */
	@Override
	public Map<Long, IVoiceChannel> getVoiceChannels() {
		return null;
	}

	/**
	 * @return
	 */
	@Override
	public boolean isSharded() {
		return false;
	}

	/**
	 * @return
	 */
	@Override
	public CompletableFuture<IClient> login() {
		return null;
	}

	/**
	 * @param token
	 * @return
	 */
	@Override
	public CompletableFuture<IClient> login(String token) {
		return null;
	}

	/**
	 * @return
	 */
	@Override
	public CompletableFuture<Void> disconnect() {
		return null;
	}

	/**
	 * @param reason
	 * @param code
	 * @return
	 */
	@Override
	public CompletableFuture<Void> disconnect(String reason, int code) {
		return null;
	}

}
