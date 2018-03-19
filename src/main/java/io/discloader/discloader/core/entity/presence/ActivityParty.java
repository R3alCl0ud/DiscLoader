package io.discloader.discloader.core.entity.presence;

import io.discloader.discloader.entity.presence.IActivityParty;
import io.discloader.discloader.network.json.ActivityPartyJSON;

public class ActivityParty implements IActivityParty {

	private int[] size;
	private String id;

	public ActivityParty(ActivityPartyJSON data) {
		this.id = data.id;
		this.size = data.size;
	}

	@Override
	public int getCurrentSize() {
		return size[0];
	}

	@Override
	public String getID() {
		return id;
	}

	@Override
	public int getMaxSize() {
		return size[1];
	}

}
