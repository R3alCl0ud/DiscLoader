package io.discloader.discloader.common.start;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TimerTask;

import com.google.gson.Gson;

import io.discloader.discloader.client.command.CommandHandler;
import io.discloader.discloader.client.logger.ProgressLogger;
import io.discloader.discloader.client.render.WindowFrame;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.discovery.ModCandidate;
import io.discloader.discloader.common.discovery.ModDiscoverer;
import io.discloader.discloader.common.event.ChannelCreateEvent;
import io.discloader.discloader.common.event.ChannelUpdateEvent;
import io.discloader.discloader.common.event.DLPreInitEvent;
import io.discloader.discloader.common.event.EventAdapter;
import io.discloader.discloader.common.event.MessageCreateEvent;
import io.discloader.discloader.common.logger.FileLogger;
import io.discloader.discloader.common.registry.ModRegistry;
import io.discloader.discloader.util.Constants;

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
	private static FileLogger LOG;
	
	
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String... args) throws IOException {
		System.out.println(Constants.Test.TEXT);
		LOG = new FileLogger();
		DiscLoader.addEventHandler(new EventAdapter() {
			@Override
			public void raw(String text) {

			}
			
			@Override
			public void PreInit(DLPreInitEvent e) {
				try {
					LOG.log(String.format("Active Mod: %s", e.activeMod.modInfo.modid()));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
			@Override
			public void MessageCreate(MessageCreateEvent e) {
				try {
					LOG.log(String.format("Author: %s#%s, Channel: %s", e.message.author.username, e.message.author.discriminator, e.message.channel.id));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
			@Override
			public void ChannelCreate(ChannelCreateEvent e) {
				try {
					LOG.log(String.format("New Channel Created: %s", e.channel.name));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
			@Override
			public void ChannelUpdate(ChannelUpdateEvent e) {
				try {
					LOG.log(String.format("Channel Updated: %s", e.channel.name));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		String content = "";
		Object[] lines = Files.readAllLines(Paths.get("./options.json")).toArray();
		for (Object line : lines)
			content += line;
		char[] chars = {'a', 'b', 'c'};
		System.out.println(chars.toString().indexOf('b'));
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
					System.out.println("Expected argument after -t");
					System.exit(1);
				}
			} else if (args[i].equals("-p")) {
				if (i + 1 < args.length) {
					CommandHandler.prefix = args[i + 1];
				} else {
					System.out.println("Expected argument after -p");
					System.exit(1);
				}
			}
		}
	}

	/**
	 * @return the lOG
	 */
	public static FileLogger getLOGGER() {
		return LOG;
	}
	
	public static DiscLoader getLoader() {
		return loader;
	}
}
