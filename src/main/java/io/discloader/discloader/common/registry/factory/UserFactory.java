package io.discloader.discloader.common.registry.factory;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.core.entity.user.User;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.json.UserJSON;

public class UserFactory {

	public IUser buildUser(UserJSON data) {
		return new User(DiscLoader.getDiscLoader(), data);
	}
}
