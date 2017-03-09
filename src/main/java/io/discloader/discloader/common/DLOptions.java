package io.discloader.discloader.common;

/**
 * Options to be passed to a new instance of DiscLoader
 * 
 * @author Perry Berman
 */
public class DLOptions {

	/**
	 * The token to use when logging in
	 */
	public String token = "TOKEN";

	/**
	 * The prefix to look for
	 */
	public String prefix = "/";

	/**
	 * Should the client use the provided commands?
	 */
	public boolean defaultCommands = true;

	/**
	 * Do you want the loading window to be shown?
	 */
	public boolean useWindow = true;

	public int shard = 0;

	public int shards = 1;

	public DLOptions(boolean defaultCommands, boolean useWindow) {
		this("TOKEN", "/", defaultCommands, useWindow, 0, 1);
	}

	public DLOptions(int shard, int shards) {
		this("TOKEN", "/", true, true, 0, 1);
	}

	public DLOptions(String token, String prefix) {
		this(token, prefix, true, true, 0, 1);
	}

	public DLOptions(String token, String prefix, boolean defaultCommands, boolean useWindow) {
		this(token, prefix, defaultCommands, useWindow, 0, 1);
	}

	public DLOptions(String token, String prefix, boolean defaultCommands, boolean useWindow, int shard, int shards) {
		this.token = token;
		this.prefix = prefix;
		this.defaultCommands = defaultCommands;
		this.useWindow = useWindow;
		this.shard = shard;
		this.shards = shards;
	}

	public DLOptions(String token, String prefix, int shard, int shards) {
		this(token, prefix, true, true, shard, shards);
	}
}
