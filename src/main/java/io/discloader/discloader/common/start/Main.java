package io.discloader.discloader.common.start;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimerTask;
import java.util.logging.Logger;

import com.google.gson.Gson;

import io.discloader.discloader.client.command.CommandHandler;
import io.discloader.discloader.client.logger.DLLogger;
import io.discloader.discloader.client.logger.ProgressLogger;
import io.discloader.discloader.client.render.WindowFrame;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.discovery.ModCandidate;
import io.discloader.discloader.common.discovery.ModDiscoverer;
import io.discloader.discloader.common.event.ChannelCreateEvent;
import io.discloader.discloader.common.event.ChannelUpdateEvent;
import io.discloader.discloader.common.event.DLPreInitEvent;
import io.discloader.discloader.common.event.EventListenerAdapter;
import io.discloader.discloader.common.event.GuildBanAddEvent;
import io.discloader.discloader.common.event.MessageCreateEvent;
import io.discloader.discloader.common.language.Language;
import io.discloader.discloader.common.language.LanguageRegistry;
import io.discloader.discloader.common.logger.DLErrorStream;
import io.discloader.discloader.common.logger.DLPrintStream;
import io.discloader.discloader.common.registry.ModRegistry;

/**
 * DiscLoader client entry point
 * 
 * @author Perry Berman
 * @see DiscLoader
 */
public class Main {
	public static final Gson gson = new Gson();
	public static WindowFrame window;
	public static final DiscLoader loader = new DiscLoader();
	public static boolean usegui = false;
	public static String token;
	private static Logger LOGGER;

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String... args) throws IOException {
		LOGGER = new DLLogger("Main Thread").getLogger();
		System.setOut(new DLPrintStream(System.out, LOGGER));
		System.setErr(new DLErrorStream(System.err, LOGGER));
		DiscLoader.addEventHandler(new EventListenerAdapter() {
			@Override
			public void raw(String text) {
				// LOG.warning(text);
			}

			@Override
			public void GuildBanAdd(GuildBanAddEvent e) {
				LOGGER.fine(e.member.user.username);
				e.guild.getDefaultChannel()
						.sendMessage(String.format("%s was banned from the server", e.member.toString()));
			}

			@Override
			public void Ready(DiscLoader loader) {
				LOGGER.fine(String.format("Ready as user %s#%s", loader.user.username, loader.user.discriminator));
				System.out.print(LanguageRegistry.getLocalized("gui.tabcommands.name"));
			}

			@Override
			public void PreInit(DLPreInitEvent e) {
				LOGGER.fine(String.format("Active Mod: %s", e.activeMod.modInfo.modid()));
			}

			@Override
			public void MessageCreate(MessageCreateEvent e) {
				LOGGER.fine(String.format("Author: %s#%s, Channel: %s", e.message.author.username,
						e.message.author.discriminator, e.message.channel.id));
			}

			@Override
			public void ChannelCreate(ChannelCreateEvent e) {
				LOGGER.fine(String.format("New Channel Created: %s", e.channel.name));

			}

			@Override
			public void ChannelUpdate(ChannelUpdateEvent e) {
				LOGGER.fine(String.format("Channel Updated: %s", e.channel.name));
			}
		});

		String content = "";
		Object[] lines = Files.readAllLines(Paths.get("./options.json")).toArray();
		for (Object line : lines)
			content += line;
		options options = gson.fromJson(content, options.class);
		token = options.auth.token;
		parseArgs(args);

		if (usegui) {
			window = new WindowFrame(loader);
		} else {
			ProgressLogger.stage(1, 3, "Mod Discovery");
			ModDiscoverer.checkModDir();
			ArrayList<ModCandidate> candidates = ModDiscoverer.discoverMods();
			TimerTask checkCandidates = new TimerTask() {

				@Override
				public void run() {
					ProgressLogger.stage(2, 3, "Discovering Mod Containers");
					ModRegistry.checkCandidates(candidates);
				}

			};
			loader.timer.schedule(checkCandidates, 500);
		}
	}

	public static void parseArgs(String... args) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase("usegui")) {
				usegui = true;
			} else if (args[i].equals("-t")) {
				if (i + 1 < args.length) {
					token = args[i + 1];
				} else {
					LOGGER.severe("Expected argument after -t");
					System.exit(1);
				}
			} else if (args[i].equals("-p")) {
				if (i + 1 < args.length) {
					CommandHandler.prefix = args[i + 1];
				} else {
					LOGGER.severe("Expected argument after -p");
					System.exit(1);
				}
			}
		}
	}

	/**
	 * @return the lOG
	 */
	public static Logger getLogger() {
		return LOGGER;
	}

	public static DiscLoader getLoader() {
		return loader;
	}
}
