package io.discloader.discloader.network.gateway.packets.request;

import io.discloader.discloader.core.entity.presence.Activity;

public class StatusUpdate {
	public Activity game;
	public boolean afk;
	public String status;
	public int since;

	public StatusUpdate(Activity activity, String status, boolean afk, int since) {
		this.game = activity;
		this.since = since;
		this.afk = afk;
		this.status = status;
	}
}
