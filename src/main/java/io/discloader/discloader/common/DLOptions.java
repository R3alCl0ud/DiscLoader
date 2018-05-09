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

	private boolean debug = false;

	public boolean selfbot = false;

	private boolean autoExecRestActions = true;

	public int shard = 0;

	public int shards = 1;

	public DLOptions() {
		this("TOKEN", "/", true, false, false, true, 0, 1);
	}

	public DLOptions(boolean defaultCommands) {
		this("TOKEN", "/", defaultCommands, false, false, true, 0, 1);
	}

	public DLOptions(boolean defaultCommands, boolean loadMods) {
		this("TOKEN", "/", defaultCommands, loadMods, false, true, 0, 1);
	}

	public DLOptions(boolean defaultCommands, boolean loadMods, boolean debug) {
		this("TOKEN", "/", defaultCommands, loadMods, debug, true, 0, 1);
	}

	public DLOptions(int shard, int shards) {
		this("TOKEN", "/", true, false, false, true, shard, shards);
	}

	public DLOptions(String token) {
		this(token, "/", true, false, false, true, 0, 1);
	}

	public DLOptions(String token, String prefix) {
		this(token, prefix, true, false, false, true, 0, 1);
	}

	public DLOptions(String token, String prefix, boolean defaultCommands) {
		this(token, prefix, defaultCommands, false, false, true, 0, 1);
	}

	public DLOptions(String token, String prefix, boolean defaultCommands, boolean loadMods, boolean debug, boolean autoExecRestAction, int shard, int shards) {
		this.token = token;
		this.prefix = prefix;
		this.defaultCommands = defaultCommands;
		this.useModloader = loadMods;
		this.debug = debug;
		this.shard = shard;
		this.shards = shards;
		this.autoExecRestActions = autoExecRestAction;
	}

	public DLOptions(String token, String prefix, boolean defaultCommands, boolean loadMods, int shard, int shards) {
		this(token, prefix, defaultCommands, loadMods, false, true, 0, 1);
	}

	public DLOptions(String token, String prefix, boolean defaultCommands, int shard, int shards) {
		this(token, prefix, defaultCommands, false, false, true, shard, shards);
	}

	public DLOptions(String token, String prefix, int shard, int shards) {
		this(token, prefix, true, false, false, true, shard, shards);
	}

	public int getShardCount() {
		return shards;
	}

	public int getShardID() {
		return shard;
	}

	/**
	 * Returns whether or not the client is running in debug mode
	 * 
	 * @return debug
	 */
	public boolean isDebugging() {
		return debug;
	}

	public boolean isSelfbot() {
		return selfbot;
	}

	public boolean isSharded() {
		return shards > 1;
	}

	/**
	 * @return {@code useModloader}
	 * @deprecated Use {@link #shouldLoadMods()} instead
	 */
	public boolean isUsingModloader() {
		return shouldLoadMods();
	}

	/**
	 * @return {@code useModloader}
	 */
	public boolean shouldLoadMods() {
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

	/**
	 * Set whether or not the client is running in debug mode
	 * 
	 * @param debug
	 *            whether or not the client should run in debug mode
	 */
	public DLOptions setDebug(boolean debug) {
		this.debug = debug;
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

	/**
	 * @return the autoExecuteRestActions
	 */
	public boolean autoExecRestActions() {
		return autoExecRestActions;
	}

	/**
	 * @param autoExecuteRestActions
	 *            the autoExecuteRestActions to set
	 */
	public void setAutoExecuteRestActions(boolean autoExecRestActions) {
		this.autoExecRestActions = autoExecRestActions;
	}

}
