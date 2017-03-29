package io.discloader.discloader.common.entity.user;

import java.util.HashMap;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.entity.guild.Guild;
import io.discloader.discloader.entity.user.IUserProfile;
import io.discloader.discloader.network.json.ConnectionJSON;
import io.discloader.discloader.network.json.MutualGuildJSON;
import io.discloader.discloader.network.json.ProfileJSON;

/**
 * @author Perry Berman
 */
public class UserProfile implements IUserProfile {

	private final DiscLoader loader;
	private ProfileJSON data;

	public UserProfile(DiscLoader loader, ProfileJSON data) {
		this.loader = loader;
		this.data = data;
	}

	public HashMap<String, UserConnection> getConnections() {
		HashMap<String, UserConnection> connections = new HashMap<>();
		for (ConnectionJSON d : data.connected_accounts) {
			connections.put(d.id, new UserConnection(d));
		}
		return connections;
	}

	public User getUser() {
		if (!loader.users.containsKey(data.user.id)) {
			return loader.addUser(data.user);
		} else {
			return loader.users.get(data.user.id);
		}
	}

	public HashMap<String, Guild> getMutualGuilds() {
		HashMap<String, Guild> guilds = new HashMap<>();
		for (MutualGuildJSON d : data.mutual_guilds) {
			guilds.put(d.id, loader.guilds.get(d.id));
		}
		return guilds;
	}

	public boolean isNitro() {
		return data.premium_since != null;
	}
}
