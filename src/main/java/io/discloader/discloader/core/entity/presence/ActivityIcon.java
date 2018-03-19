package io.discloader.discloader.core.entity.presence;

import java.net.MalformedURLException;
import java.net.URL;

import io.discloader.discloader.entity.IIcon;
import io.discloader.discloader.network.util.Endpoints;

public class ActivityIcon implements IIcon {

	private String hash;
	private long appID;

	public ActivityIcon(String hash, long appID) {
		this.hash = hash;
		this.appID = appID;
	}

	@Override
	public String getHash() {
		return hash;
	}

	@Override
	public URL toURL() throws MalformedURLException {
		return new URL(Endpoints.appAssets(appID, hash) + ".png");
	}

	@Override
	public String toString() {
		try {
			return toURL().toString();
		} catch (MalformedURLException ignored) {
			return null;
		}
	}
}
