package io.discloader.discloader.core.entity.user;

import io.discloader.discloader.entity.user.IUserConnection;
import io.discloader.discloader.network.json.ConnectionJSON;

/**
 * @author Perry Berman
 */
public class UserConnection implements IUserConnection {

	private String type;
	private String name;
	private String id;

	public UserConnection(ConnectionJSON data) {
		type = data.type;
		name = data.name;
		id = data.id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	@Override
	public String getID() {
		return id;
	}
}
