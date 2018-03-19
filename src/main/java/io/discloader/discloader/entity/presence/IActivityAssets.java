package io.discloader.discloader.entity.presence;

import io.discloader.discloader.entity.IIcon;

public interface IActivityAssets {
	IIcon getLargeImage();

	String getLargeText();

	IIcon getSmallImage();

	String getSmallText();
}
