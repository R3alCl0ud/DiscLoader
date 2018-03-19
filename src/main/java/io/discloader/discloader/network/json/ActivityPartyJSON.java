package io.discloader.discloader.network.json;

public class ActivityPartyJSON {

	/**
	 * The id of the party
	 */
	public String id;

	/**
	 * An array of two integers {@code [current_size, max_size]} used to show the
	 * party's current and maximum size
	 */
	public int[] size;
}
