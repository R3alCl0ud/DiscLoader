package io.discloader.discloader.common.start;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;

import io.discloader.discloader.client.renderer.WindowFrame;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.registry.DiscRegistry;

/**
 * DiscLoader client entry point
 * 
 * @author Perry Berman
 * @see DiscLoader
 */
public class Start {
	public static final Gson gson = new Gson();
	public static WindowFrame window;
	public static final DiscLoader loader = new DiscLoader();
	public static boolean nogui = false;
	public static String token;

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String... args) throws IOException {
		String content = "";
		Object[] lines = Files.readAllLines(Paths.get("./options.json")).toArray();
		for (Object line : lines)
			content += line;
		options options = gson.fromJson(content, options.class);
		nogui = options.useWindow;
		token = options.auth.token;

		if (!nogui) {
			window = new WindowFrame(loader);
		} else {
			loader.login(token);
		}
	}
	
	public static void parseArgs(String... args) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase("nogui")) {
				nogui = false;
			} else if (args[i].equals("-t")) {
				if (i + 1 < args.length) {
					token = args[i + 1];
				} else {
					System.out.println("Expected argument after -t");
					System.exit(1);
				}
			} else if (args[i].equals("-p")) {
				if (i + 1 < args.length) {
					DiscRegistry.prefix = args[i + 1];
				} else {
					System.out.println("Expected argument after -p");
					System.exit(1);
				}
			}
		}
	}
}
