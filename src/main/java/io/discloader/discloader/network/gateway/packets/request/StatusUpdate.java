package io.discloader.discloader.network.gateway.packets.request;

import io.discloader.discloader.core.entity.presence.Activity;

public class StatusUpdate {
	public Activity activity;
	public boolean afk;
	public String status;
	public int since;

	public StatusUpdate(Activity activity, String status, boolean afk, int since) {
		this.activity = activity;
		this.since = since;
		this.afk = afk;
		this.status = status;
	}
}
