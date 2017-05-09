package io.discloader.discloader.core.entity.user;

import java.net.MalformedURLException;
import java.net.URL;

import io.discloader.discloader.entity.IIcon;
import io.discloader.discloader.util.DLUtil.Endpoints;

public class UserAvatar implements IIcon {

	private String hash;
	private long userID;

	public UserAvatar(String hash, long userID) {
		this.hash = hash;
		this.userID = userID;
	}

	@Override
	public URL toURL() throws MalformedURLException {
		return new URL(Endpoints.avatar(userID, hash));
	}

	@Override
	public String getHash() {
		return hash;
	}

}
