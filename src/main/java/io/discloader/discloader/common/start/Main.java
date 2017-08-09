package io.discloader.discloader.common.start;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
// import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocketFrame;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.client.render.WindowFrame;
import io.discloader.discloader.common.DLOptions;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.Shard;
import io.discloader.discloader.common.ShardManager;
import io.discloader.discloader.common.event.EventListenerAdapter;
import io.discloader.discloader.common.event.RawEvent;
import io.discloader.discloader.common.event.ReadyEvent;
import io.discloader.discloader.common.event.message.MessageCreateEvent;
import io.discloader.discloader.common.event.sharding.ShardingListenerAdapter;
import io.discloader.discloader.common.registry.CommandRegistry;

/**
 * DiscLoader client entry point
 * 
 * @author Perry Berman
 * @see DiscLoader
 */
public class Main {

	public static final Gson gson = new Gson();

	public static WindowFrame window;

	public static DiscLoader loader;

	public static boolean usegui = false;

	public static String token;

	// private static int shard = 0, shards = 1;

	private static Logger LOGGER;

	private static final String tokenRegex = "-t=(.*+)", prefixRegex = "-p=(.*+)", shardRegex = "-s=(\\d+)\\:(\\d+)";
	private static final Pattern tokenPat = Pattern.compile(tokenRegex), prefixPat = Pattern.compile(prefixRegex), shardPat = Pattern.compile(shardRegex);

	public static DiscLoader getLoader() {
		return loader;
	}

	/**
	 * @return the lOG
	 */
	public static Logger getLogger() {
		return LOGGER;
	}

	public static void main(String... args) throws IOException {
		LOGGER = DiscLoader.LOG;
		// System.setOut(new DLPrintStream(System.out, LOGGER));
		// System.setErr(new DLErrorStream(System.err, LOGGER));
		System.setProperty("http.agent", "DiscLoader");
		String content = "";
		if (new File("options.json").exists()) {
			Object[] lines = Files.readAllLines(Paths.get("./options.json")).toArray();
			for (Object line : lines)
				content += line;
			options options = gson.fromJson(content, options.class);
			token = options.auth.token;
		}
		DLOptions options = parseArgs(args);
		if (options.shards > 1) {
			ShardManager manager = new ShardManager(options);
			manager.addShardingListener(new ShardingListenerAdapter() {

				public void ShardLaunched(Shard shard) {
					LOGGER.info(String.format("Shard #%d: Launched", shard.getShardID()));
					shard.getLoader().addEventHandler(new EventListenerAdapter() {

						public void Ready(ReadyEvent e) {
							
							for (Command command : CommandRegistry.commands.entries()) {
								LOGGER.info(command.getUnlocalizedName());
							}
						}

						@Override
						public void RawPacket(RawEvent data) {
							WebSocketFrame frame = data.getFrame();
							if (data.isGateway() && frame.isTextFrame() && !frame.getPayloadText().contains("PRESENCE_UPDATE")) {
								// LOGGER.fine(frame.getPayloadText());
							} else if (data.isREST()) {
								LOGGER.info(data.getHttpResponse().getBody());
								LOGGER.info("" + data.getHttpResponse().getStatus());
							}
						}
					});
				}
			});
			manager.launchShards();
		} else {
			loader = new DiscLoader(options);
			loader.addEventHandler(new EventListenerAdapter() {

				@Override
				public void MessageCreate(MessageCreateEvent e) {
					if (e.getMessage().getContent().equals("#$close")) {
						e.getLoader().socket.ws.disconnect(1001);
					}
				}

				public void Ready(ReadyEvent e) {
					LOGGER.info(e.getLoader().user.getUsername());
					
					for (Command command : CommandRegistry.commands.entries()) {
						LOGGER.info(command.getUnlocalizedName());
					}
				}

				@Override
				public void RawPacket(RawEvent data) {
					WebSocketFrame frame = data.getFrame();
					if (data.isGateway() && frame.isTextFrame() && !frame.getPayloadText().contains("PRESENCE_UPDATE")) {
						// LOGGER.fine(frame.getPayloadText());
					} else if (data.isREST() && data.getHttpResponse() != null) {
						LOGGER.info(data.getHttpResponse().getBody());
						// LOGGER.info("" + data.getHttpResponse().getStatus());
					}
				}
			});
			loader.login();
		}
	}

	public static DLOptions parseArgs(String... args) {
		DLOptions options = new DLOptions(false, false);
		for (String arg : args) {
			if (arg.startsWith("-") && !arg.startsWith("--") && !arg.contains("=")) {
				if (arg.contains("d")) {
					options = new DLOptions(options.token, options.prefix, true, options.useWindow, options.shard, options.shards);
				}
				if (arg.contains("g")) {
					options = new DLOptions(options.token, options.prefix, options.defaultCommands, true, options.shard, options.shards);
				}
			} else if (arg.equals("usegui")) {
				options = new DLOptions(options.token, options.prefix, options.defaultCommands, true, options.shard, options.shards);
			} else if (arg.equalsIgnoreCase("--defaultcmd")) {
				options = new DLOptions(options.token, options.prefix, true, options.useWindow, options.shard, options.shards);
			}
			Matcher tokenMatcher = tokenPat.matcher(arg), prefixMatcher = prefixPat.matcher(arg), shardMatcher = shardPat.matcher(arg);
			if (tokenMatcher.find()) {
				options.setToken(tokenMatcher.group(1));
			} else if (prefixMatcher.find()) {
				options.setPrefix(prefixMatcher.group(1));
			} else if (shardMatcher.find()) {
				if (shardMatcher.groupCount() != 2) {
					LOGGER.severe("Sharding option usage: -s=shard:totalshards");
					continue;
				}

				options.setSharding(Integer.parseInt(shardMatcher.group(1)), Integer.parseInt(shardMatcher.group(2)));
				// System.out.println();
			}
		}
		return options;
	}
}
