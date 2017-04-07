package io.discloader.discloader.common.registry;

import io.discloader.discloader.common.registry.factory.GuildFactory;
import io.discloader.discloader.common.registry.factory.UserFactory;

/**
 * @author Perry Berman
 */
public class FactoryManager {

	public static final FactoryManager instance = new FactoryManager();

	private GuildFactory guildFactory = new GuildFactory();
	private UserFactory userFactory = new UserFactory();

	public GuildFactory getGuildFactory() {
		return guildFactory;
	}

	public void setGuildFactory(GuildFactory factory) {
		guildFactory = factory;
	}

	/**
	 * @return the userFactory
	 */
	public UserFactory getUserFactory() {
		return userFactory;
	}

	/**
	 * @param userFactory the userFactory to set
	 */
	public void setUserFactory(UserFactory userFactory) {
		this.userFactory = userFactory;
	}

}
