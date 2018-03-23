package io.discloader.discloader.core.entity.presence;

import java.net.MalformedURLException;
import java.net.URL;

import io.discloader.discloader.entity.presence.ActivityType;
import io.discloader.discloader.entity.presence.IActivity;
import io.discloader.discloader.entity.presence.IActivityAssets;
import io.discloader.discloader.entity.presence.IActivityParty;
import io.discloader.discloader.entity.presence.IActivityTimestamps;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.ActivityJSON;

public class Activity implements IActivity {

	/**
	 * The name of the game being played or streamed
	 */
	private String name;

	/**
	 * The type of activity
	 */
	private int type;

	/**
	 * The link to the user's Twitch Stream. (Currently it is only possible to
	 * stream using Twitch on discord)<br>
	 * is null if {@link #isStream()} is {@code false}
	 */
	private String url;

	private transient String state, details;
	private transient IActivityParty party;
	private transient IActivityAssets assets;
	private transient IActivityTimestamps timestamps;
	private transient long appID;

	public Activity(ActivityJSON data) {
		this.name = data.name;
		this.type = data.type;
		this.url = data.url;
		if (data.party != null) {
			this.party = new ActivityParty(data.party);
		}
		if (data.application_id != null) {
			this.appID = SnowflakeUtil.parse(data.application_id);
		}

		if (data.assets != null) {
			this.assets = new ActivityAssets(data.assets, this.appID);
		}
		if (data.timestamps != null) {
			this.timestamps = new ActivityTimestamps(data.timestamps);
		}
		if (data.details != null)
			this.details = data.details;
		if (data.state != null)
			this.state = data.state;
	}

	public Activity(IActivity activity) {
		this.name = "" + activity.getName();
		this.type = activity.getActivityType().toInt();
		this.url = "" + activity.getURL();
		this.party = activity.getParty();
		this.state = "" + activity.getState();
		this.details = "" + activity.getDetails();
		this.timestamps = activity.getTimestamps();
		this.assets = activity.getAssets();
		this.appID = activity.getApplicationID();
	}

	public Activity(String name) {
		this.name = name;
		this.type = 0;
		this.url = null;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IActivity))
			return false;
		IActivity game = (IActivity) obj;

		return name.equals(game.getName()) && isStreaming() == game.isStreaming() && url.equals(game.getURL());
	}

	@Override
	public ActivityType getActivityType() {
		return ActivityType.getActivityTypeFromInt(type);
	}

	@Override
	public long getApplicationID() {
		return appID;
	}

	@Override
	public IActivityAssets getAssets() {
		return assets;
	}

	@Override
	public String getDetails() {
		return details;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IActivityParty getParty() {
		return party;
	}

	@Override
	public String getState() {
		return state;
	}

	@Override
	public IActivityTimestamps getTimestamps() {
		return timestamps;
	}

	@Override
	public String getURL() {
		return url;
	}

	public int hashCode() {
		return (name + type + url).hashCode();
	}

	@Override
	public boolean isGame() {
		return getActivityType() == ActivityType.GAME;
	}

	@Override
	public boolean isListening() {
		return getActivityType() == ActivityType.LISTENING;
	}

	/**
	 * Checks if {@code this} is a live stream
	 * 
	 * @return true {@code if }{@link #type} {@code == 1}
	 */
	public boolean isStream() {
		return type != 0 && type == 1;
	}

	@Override
	public boolean isStreaming() {
		return false;
	}

	@Override
	public URL toURL() throws MalformedURLException {
		return new URL(url);
	}
}
