package io.discloader.discloader.common.start;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
// import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocketFrame;

import io.discloader.discloader.client.render.WindowFrame;
import io.discloader.discloader.common.DLOptions;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.event.EventListenerAdapter;
import io.discloader.discloader.common.event.RawEvent;
import io.discloader.discloader.common.event.ReadyEvent;
import io.discloader.discloader.common.event.guild.member.GuildMemberUpdateEvent;
import io.discloader.discloader.common.logger.DLErrorStream;
import io.discloader.discloader.common.logger.DLPrintStream;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.core.entity.RichEmbed;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.guild.IGuildMember;

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

	private static final String tokenRegex = "-t=(.*+)", prefixRegex = "-p=(.*+)", shardRegex = "-s=(\\d+):(\\d+)";
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
		System.setOut(new DLPrintStream(System.out, LOGGER));
		System.setErr(new DLErrorStream(System.err, LOGGER));
		System.setProperty("http.agent", "DiscLoader");
		String content = "";
		if (new File("options.json").exists()) {
			Object[] lines = Files.readAllLines(Paths.get("./options.json")).toArray();
			for (Object line : lines)
				content += line;
			options options = gson.fromJson(content, options.class);
			token = options.auth.token;
		}
		loader = new DiscLoader(parseArgs(args));
		loader.addEventHandler(new EventListenerAdapter() {

			@Override
			public void RawPacket(RawEvent data) {
				if (data.isGateway()) {
					WebSocketFrame frame = data.getFrame();
					if (frame.isTextFrame()) System.out.println(frame.getPayloadText());
				}
			}

			@Override
			public void Ready(ReadyEvent event) {
				System.out.printf("Ready as user: %s", loader.user);
			}

			@Override
			public void GuildMemberUpdate(GuildMemberUpdateEvent event) {
				IGuildMember member = event.member, oldMember = event.oldMember;
				if (!member.getNickname().equals(oldMember.getNickname())) {
					ITextChannel testChannel = EntityRegistry.getTextChannelByID(282230026869669888L);
					RichEmbed embed = new RichEmbed("Member Updated").setColor(0xfefa2a);
					embed.addField("Nickname Updated", String.format("**%s**(ID: `%d`) has changed their nickname to **%s** from **%s**", member.toString(), member.getID(), member.getNickname(), oldMember.getNickname()));
					embed.setTimestamp(OffsetDateTime.now());
					testChannel.sendEmbed(embed);
				}
			}
		});
		loader.login();

	}

	public static DLOptions parseArgs(String... args) {
		DLOptions options = new DLOptions(true, false);
		for (String arg : args) {
			if (arg.startsWith("-") && !arg.startsWith("--")) {
				if (arg.contains("d")) {
					options = new DLOptions(true, options.useWindow);
				}
				if (arg.contains("g")) {
					options = new DLOptions(options.defaultCommands, true);
				}
			} else if (arg.equals("usegui")) {
				options = new DLOptions(options.defaultCommands, true);
			} else if (arg.equalsIgnoreCase("--defaultcmd")) {
				options = new DLOptions(true, options.useWindow);
			}
			Matcher tokenMatcher = tokenPat.matcher(arg), prefixMatcher = prefixPat.matcher(arg), shardMatcher = shardPat.matcher(arg);
			if (tokenMatcher.find()) {
				options.setToken(tokenMatcher.group(1));
			} else if (prefixMatcher.find()) {
				options.setPrefix(prefixMatcher.group(1));
			} else if (shardMatcher.find()) {
				if (shardMatcher.groupCount() != 3) {
					LOGGER.severe("Sharding option usage: -s=shard:totalshards");
					continue;
				}
				options.setSharding(Integer.parseInt(shardMatcher.group(1)), Integer.parseInt(shardMatcher.group(3)));
			}
		}
		return options;
	}
}
