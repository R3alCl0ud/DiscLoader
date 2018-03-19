package io.discloader.discloader.core.entity.presence;

import io.discloader.discloader.entity.IIcon;
import io.discloader.discloader.entity.presence.IActivityAssets;
import io.discloader.discloader.network.json.ActivityAssetsJSON;

public class ActivityAssets implements IActivityAssets {

	private String small_text, large_text, small_image, large_image;
	private long appID;

	public ActivityAssets(ActivityAssetsJSON data, long appID) {
		this.large_image = data.large_image;
		this.large_text = data.large_text;
		this.small_image = data.small_image;
		this.small_text = data.small_text;
	}

	@Override
	public IIcon getLargeImage() {
		return new ActivityIcon(large_image, appID);
	}

	@Override
	public String getLargeText() {
		return large_text;
	}

	@Override
	public IIcon getSmallImage() {
		return new ActivityIcon(small_image, appID);
	}

	@Override
	public String getSmallText() {
		return small_text;
	}

}
