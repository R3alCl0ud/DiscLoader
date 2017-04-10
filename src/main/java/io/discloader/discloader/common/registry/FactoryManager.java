package io.discloader.discloader.common.registry;

import io.discloader.discloader.common.registry.factory.ChannelFactory;
import io.discloader.discloader.common.registry.factory.GuildFactory;
import io.discloader.discloader.common.registry.factory.InviteFactory;
import io.discloader.discloader.common.registry.factory.UserFactory;

/**
 * @author Perry Berman
 */
public class FactoryManager {

	public static final FactoryManager instance = new FactoryManager();

	private GuildFactory guildFactory = new GuildFactory();
	private UserFactory userFactory = new UserFactory();
	private ChannelFactory channelFactory = new ChannelFactory();
	private InviteFactory inviteFactory = new InviteFactory();

	/**
	 * @return the channelFactory
	 */
	public ChannelFactory getChannelFactory() {
		return channelFactory;
	}

	public GuildFactory getGuildFactory() {
		return guildFactory;
	}

	/**
	 * @return the inviteFactory
	 */
	public InviteFactory getInviteFactory() {
		return inviteFactory;
	}

	/**
	 * @return the userFactory
	 */
	public UserFactory getUserFactory() {
		return userFactory;
	}

	/**
	 * @param channelFactory the channelFactory to set
	 */
	public void setChannelFactory(ChannelFactory channelFactory) {
		this.channelFactory = channelFactory;
	}

	public void setGuildFactory(GuildFactory factory) {
		guildFactory = factory;
	}

	/**
	 * @param inviteFactory the inviteFactory to set
	 */
	public void setInviteFactory(InviteFactory inviteFactory) {
		this.inviteFactory = inviteFactory;
	}

	/**
	 * @param userFactory the userFactory to set
	 */
	public void setUserFactory(UserFactory userFactory) {
		this.userFactory = userFactory;
	}

}
