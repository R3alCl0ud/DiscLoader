package io.discloader.discloader.network.json;

public class ActivityJSON {
	public int type;
	/**
	 * activity flags <code>OR<code>d together, describes what the payload includes
	 */
	public int flags;

	/**
	 * whether or not the activity is an instanced game session
	 */
	public boolean instance;

	public String name, url, application_id, details, state;
	/**
	 * Information for the current party of the player. can be {@code null}.
	 */
	public ActivityPartyJSON party;

	/**
	 * Images for the presence and their hover texts. Can be {@code null}
	 */
	public ActivityAssetsJSON assets;

	/**
	 * Unix timestamps for start and/or end of the game
	 */
	public ActivityTimestampsJSON timestamps;
}
