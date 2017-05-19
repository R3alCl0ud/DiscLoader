package io.discloader.discloader.core.entity.user;

import java.net.MalformedURLException;
import java.net.URL;

import io.discloader.discloader.entity.IIcon;
import io.discloader.discloader.util.DLUtil.Endpoints;

public class UserAvatar implements IIcon {

	private String hash;
	private long userID;
	private int discrim;

	public UserAvatar(String hash, long userID, int discrim) {
		this.hash = hash;
		this.userID = userID;
	}

	@Override
	public URL toURL() throws MalformedURLException {
		if (hash == null) return new URL(Endpoints.defaultAvatar(discrim % 5));
		return new URL(Endpoints.avatar(userID, hash));
	}

	public String toString() {
		if (hash == null) return Endpoints.defaultAvatar(discrim % 5);
		return Endpoints.avatar(userID, hash);
	}

	@Override
	public String getHash() {
		return hash;
	}

}
