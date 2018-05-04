package io.discloader.discloader.core.entity.presence;

import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.entity.presence.IActivity;
import io.discloader.discloader.entity.presence.IPresence;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.json.ActivityJSON;
import io.discloader.discloader.network.json.PresenceJSON;

/**
 * Represents a user's status on Discord
 * 
 * @author Perry Berman
 */
public class Presence implements IPresence {

	/**
	 * The {@link Activity} the user is currently playing, or null if user isn't
	 * playing a game
	 * 
	 * 
	 * @see IActivity
	 * @see IUser
	 * @see IGuildMember
	 */
	private Activity game;
	/**
	 * The status of the current user. can be {@literal online}, {@literal offline},
	 * {@literal idle}, or {@literal invisible}
	 * 
	 * 
	 */
	private String status;

	private String[] roles;

	public Presence() {
		this.game = null;
		this.status = "offline";
	}

	public Presence(Presence data) {
		this.game = data.game != null ? new Activity(data.game) : null;
		this.status = data.status;
		this.roles = data.getRoleIDs();
	}

	public Presence(PresenceJSON data) {
		this.game = data.game != null ? new Activity(data.game) : null;
		this.status = data.status;
		this.roles = data.roles;
	}

	protected Presence(String status, Activity activity) {
		this.game = activity;
		this.status = status;
	}

	public Presence(String status, ActivityJSON game) {
		this.game = new Activity(game);
		this.status = status;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IPresence))
			return false;
		IPresence p = (IPresence) obj;
		return status.equals(p.getStatus()) && game.equals(p.getActivity());
	}

	@Override
	public IActivity getActivity() {
		return game;
	}

	@Override
	public String[] getRoleIDs() {
		return roles;
	}

	@Override
	public String getStatus() {
		return status;
	}

	@Override
	public int hashCode() {
		if (game != null)
			return (status + game.hashCode()).hashCode();
		return status.hashCode();
	}

	public void update(Activity activity) {
		this.update(null, activity);
	}

	public void update(PresenceJSON presence) {
		this.update(presence.status, presence.game != null ? new Activity(presence.game) : null);
	}

	public void update(String status) {
		this.update(status, this.game);
	}

	public void update(String status, Activity activity) {
		this.status = status != null ? status : this.status;
		this.game = activity;
	}

}
