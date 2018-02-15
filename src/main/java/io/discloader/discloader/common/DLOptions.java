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

	private boolean useModloader = false;

	public boolean selfbot = false;

	public int shard = 0;

	public int shards = 1;

	public DLOptions() {
		this("TOKEN", "/", true, false, 0, 1);
	}

	public DLOptions(boolean defaultCommands) {
		this("TOKEN", "/", defaultCommands, false, 0, 1);
	}

	public DLOptions(int shard, int shards) {
		this("TOKEN", "/", true, false, shard, shards);
	}

	public DLOptions(String token) {
		this(token, "/", true, false, 0, 1);
	}

	public DLOptions(String token, String prefix) {
		this(token, prefix, true, false, 0, 1);
	}

	public DLOptions(String token, String prefix, boolean defaultCommands) {
		this(token, prefix, defaultCommands, false, 0, 1);
	}

	public DLOptions(String token, String prefix, boolean defaultCommands, boolean loadMods, int shard, int shards) {
		this.token = token;
		this.prefix = prefix;
		this.defaultCommands = defaultCommands;
		this.useModloader = loadMods;
		this.shard = shard;
		this.shards = shards;
	}

	public DLOptions(String token, String prefix, boolean defaultCommands, int shard, int shards) {
		this(token, prefix, defaultCommands, false, shard, shards);
	}

	public DLOptions(String token, String prefix, int shard, int shards) {
		this(token, prefix, true, false, shard, shards);
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

	/**
	 * @return {@code useModloader}
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

	public DLOptions useDefaultCommands(boolean b) {
		defaultCommands = b;
		return this;
	}

}
