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
	 * 
	 * <pre>
	 * default = "/";
	 * </pre>
	 */
	public String prefix = "/";

	/**
	 * Should the client use the provided commands?
	 */
	public boolean defaultCommands = true;

	/**
	 * Do you want the loading window to be shown?<br>
	 * 
	 * <pre>
	 * Default = false;
	 * </pre>
	 */
	public boolean useWindow = false;

	private boolean useModloader = false;

	public boolean selfbot = false;

	public int shard = 0;

	public int shards = 1;

	public DLOptions() {
		this("TOKEN", "/", true, false, false, 0, 1);
	}

	public DLOptions(boolean defaultCommands, boolean useWindow) {
		this("TOKEN", "/", defaultCommands, useWindow, false, 0, 1);
	}

	public DLOptions(int shard, int shards) {
		this("TOKEN", "/", true, false, false, shard, shards);
	}

	public DLOptions(String token) {
		this(token, "/", true, false, false, 0, 1);
	}

	public DLOptions(String token, String prefix) {
		this(token, prefix, true, false, false, 0, 1);
	}

	public DLOptions(String token, String prefix, boolean defaultCommands, boolean useWindow) {
		this(token, prefix, defaultCommands, useWindow, false, 0, 1);
	}

	public DLOptions(String token, String prefix, boolean defaultCommands, boolean useWindow, boolean loadMods, int shard, int shards) {
		this.token = token;
		this.prefix = prefix;
		this.defaultCommands = defaultCommands;
		this.useWindow = useWindow;
		this.useModloader = loadMods;
		this.shard = shard;
		this.shards = shards;
	}

	public DLOptions(String token, String prefix, boolean defaultCommands, boolean useWindow, int shard, int shards) {
		this(token, prefix, defaultCommands, useWindow, false, shard, shards);
	}

	public DLOptions(String token, String prefix, int shard, int shards) {
		this(token, prefix, true, false, false, shard, shards);
	}

	public int getShardCount() {
		return shards;
	}

	public int getShardID() {
		return shard;
	}

	public boolean isSelfbot() {
		return selfbot;
	}

	public boolean isSharded() {
		return shards > 1;
	}

	public boolean isShowingWindow() {
		return useWindow;
	}

	/**
	 * @return {@codeuseModloader}
	 */
	public boolean isUsingModloader() {
		return useModloader;
	}

	/**
	 * @param useModloader
	 *            the useModloader to set
	 */
	public DLOptions loadMods(boolean useModloader) {
		this.useModloader = useModloader;
		return this;
	}

	public DLOptions setPrefix(String prefix) {
		this.prefix = prefix;
		return this;
	}

	public DLOptions setSharding(int shard, int totalShards) {
		this.shard = shard;
		this.shards = totalShards;
		return this;
	}

	public DLOptions setToken(String token) {
		this.token = token;
		return this;
	}

	public DLOptions showWindow(boolean show) {
		useWindow = show;
		return this;
	}

	public DLOptions useDefaultCommands(boolean b) {
		defaultCommands = b;
		return this;
	}

}
