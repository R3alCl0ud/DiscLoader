package io.discloader.discloader.entity.presence;

import java.net.MalformedURLException;
import java.net.URL;

import io.discloader.discloader.entity.util.ISnowflake;

public interface IActivity {

	boolean equals(Object obj);

	ActivityType getActivityType();

	IActivityAssets getAssets();

	IActivityTimestamps getTimestamps();

	String getDetails();

	/**
	 * Returns the application id for the game
	 * 
	 * @return The application id for the game.
	 */
	long getApplicationID();

	String getName();

	IActivityParty getParty();

	String getState();

	String getURL();

	boolean isGame();

	boolean isListening();

	@Deprecated
	boolean isStream();

	boolean isStreaming();

	URL toURL() throws MalformedURLException;

}
