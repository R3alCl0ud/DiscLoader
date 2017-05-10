package io.discloader.discloader.entity;

import java.net.MalformedURLException;
import java.net.URL;

public interface IIcon {

	/**
	 * Returns the {@link IIcon icon's} base64 hash String.
	 * 
	 * @return The {@link IIcon icon's} base64 hash String.
	 */
	String getHash();

	/**
	 * Returns a String representation of {@link #toURL()}
	 * 
	 * @return A String representation of {@link #toURL()}
	 */
	String toString();

	/**
	 * Creates a URL object pointing to the location of {@code this} icon.
	 * 
	 * @return A URL object pointing to the location of {@code this} icon.
	 * @throws MalformedURLException Thrown to indicate that a malformed URL has
	 *             occurred. Either no legal protocol could be found in a
	 *             specification string or the string could not be parsed.
	 */
	URL toURL() throws MalformedURLException;
}
