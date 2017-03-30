package io.discloader.discloader.entity.user;

import java.util.Map;

import io.discloader.discloader.entity.guild.IGuild;

public interface IUserProfile {

	IUser getUser();
	
	Map<String, IUserConnection> getConnections();
	
	Map<String, IGuild> getMutualGuilds();
	
}
