package io.discloader.discloader.entity.presence;

import java.net.MalformedURLException;
import java.net.URL;

public interface IActivity {

	boolean equals(Object obj);

	/**
	 * Returns the {@link ActivityType}.
	 * 
	 * @return The {@link ActivityType}.
	 */
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

	/**
	 * Checks if {@code this} activity is a game.
	 * 
	 * @return {@code true} if {@link #getActivityType()} is
	 *         {@link ActivityType#GAME}, {@code false} otherwise.
	 */
	boolean isGame();

	/**
	 * Checks if {@code this} activity is music.
	 * 
	 * @return {@code true} if {@link #getActivityType()} is
	 *         {@link ActivityType#LISTENING}, {@code false} otherwise.
	 */
	boolean isListening();

	/**
	 * Checks if {@code this} activity is a live stream.
	 * 
	 * @return {@code true} if {@link #getActivityType()} is
	 *         {@link ActivityType#STREAMING}, {@code false} otherwise.
	 */
	boolean isStreaming();

	URL toURL() throws MalformedURLException;

}
