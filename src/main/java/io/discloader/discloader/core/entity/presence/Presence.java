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
	private Activity activity;
	/**
	 * The status of the current user. can be {@literal online}, {@literal offline},
	 * {@literal idle}, or {@literal invisible}
	 * 
	 * 
	 */
	private String status;

	public Presence(PresenceJSON presence) {
		this.activity = presence.game != null ? new Activity(presence.game) : null;
		this.status = presence.status;
	}

	public Presence(String status, ActivityJSON game) {
		this.activity = new Activity(game);
		this.status = status;
	}

	protected Presence(String status, Activity activity) {
		this.activity = activity;
		this.status = status;
	}

	public Presence() {
		this.activity = null;
		this.status = "offline";
	}

	public Presence(Presence data) {
		this.activity = data.activity != null ? new Activity(data.activity) : null;
		this.status = data.status;
	}

	public void update(PresenceJSON presence) {
		this.update(presence.status, presence.game != null ? new Activity(presence.game) : null);
	}

	public void update(String status, Activity activity) {
		this.status = status != null ? status : this.status;
		this.activity = activity;
	}

	public void update(String status) {
		this.update(status, this.activity);
	}

	public void update(Activity activity) {
		this.update(null, activity);
	}

	@Override
	public String getStatus() {
		return status;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof IPresence))
			return false;
		IPresence p = (IPresence) obj;
		return status.equals(p.getStatus()) && activity.equals(p.getActivity());
	}

	public int hashCode() {
		if (activity != null)
			return (status + activity.hashCode()).hashCode();
		return status.hashCode();
	}

	@Override
	public IActivity getActivity() {
		return activity;
	}

}
