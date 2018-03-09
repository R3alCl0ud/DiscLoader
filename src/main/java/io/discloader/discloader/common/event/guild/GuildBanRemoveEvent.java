package io.discloader.discloader.common.event.guild;

import io.discloader.discloader.common.event.DLEvent;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.user.IUser;

/**
 * This is event is fired when a {@link IUser user} gets unbanned from a
 * {@link IGuild guild}.
 * 
 * 
 * @author Perry Berman
 */
public class GuildBanRemoveEvent extends DLEvent {

	protected final IGuild guild;
	protected final IUser user;

	/**
	 * Creates a new GuildBanRemoveEvent Object
	 * 
	 * @param guild
	 *            The guild the user was unbanned from
	 * @param user
	 *            The use that has been unbanned
	 */
	public GuildBanRemoveEvent(IGuild guild, IUser user) {
		super(guild.getLoader());
		this.guild = guild;
		this.user = user;
	}

	/**
	 * Returns the {@link IGuild} the {@link IUser} was unbanned from.
	 * 
	 * @return The {@link IGuild} the {@link IUser} was unbanned from.
	 */
	public IGuild getGuild() {
		return guild;
	}

	/**
	 * Returns the {@link IUser} that was unbanned.
	 * 
	 * @return The {@link IUser} that was unbanned.
	 */
	public IUser getUnbannedUser() {
		return user;
	}

}
