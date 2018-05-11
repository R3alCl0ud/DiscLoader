package io.discloader.discloader.core.entity.user;

import java.util.HashMap;
import java.util.Map;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.user.IUserConnection;
import io.discloader.discloader.entity.user.IUserProfile;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.ConnectionJSON;
import io.discloader.discloader.network.json.MutualGuildJSON;
import io.discloader.discloader.network.json.ProfileJSON;

/**
 * @author Perry Berman
 */
public class UserProfile implements IUserProfile {

	private final DiscLoader loader;
	private ProfileJSON data;
	private final IUser user;
	// private MutualGuildJSON[] mutualGuilds;

	public UserProfile(DiscLoader loader, ProfileJSON data) {
		this.user = EntityRegistry.addUser(data.user);
		this.loader = loader;
		this.data = data;
		// this.mutualGuilds = data.mutual_guilds;
	}

	@Override
	public HashMap<String, IUserConnection> getConnections() {
		HashMap<String, IUserConnection> connections = new HashMap<>();
		for (ConnectionJSON d : data.connected_accounts) {
			connections.put(d.id, new UserConnection(d));
		}
		return connections;
	}

	@Override
	public IUser getUser() {
		return user;
	}

	@Override
	public Map<Long, IGuild> getMutualGuilds() {
		Map<Long, IGuild> guilds = new HashMap<>();
		for (MutualGuildJSON d : data.mutual_guilds) {
			guilds.put(SnowflakeUtil.parse(d.id), EntityRegistry.getGuildByID(d.id));
		}
		return guilds;
	}

	public boolean isNitro() {
		return data.premium_since != null;
	}

	/**
	 * @return the loader
	 */
	public DiscLoader getLoader() {
		return loader;
	}
}
