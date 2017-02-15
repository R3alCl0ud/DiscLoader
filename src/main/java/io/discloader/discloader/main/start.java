package io.discloader.discloader.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.tree.DefaultTreeModel;

import com.google.gson.Gson;

import io.discloader.discloader.DiscLoader;
import io.discloader.discloader.objects.annotations.EventHandler;
import io.discloader.discloader.objects.loader.DiscRegistry;
import io.discloader.discloader.objects.window.WindowFrame;

/**
 * DiscLoader client entry point
 * 
 * @author Perry Berman
 * @see DiscLoader
 */
public class start {
	public static final Gson gson = new Gson();
	public static WindowFrame window;
	public static final DiscLoader loader = new DiscLoader();
	public static boolean useWindow = true;
	
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
		useWindow = options.useWindow;
		String token = options.auth.token;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase("nogui")) {
				useWindow = false;
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
		if (useWindow) {
			window = new WindowFrame(loader);
		} else {			
			loader.modh.beginLoader();
			loader.login(token);
		}
	}

	@EventHandler
	public void ready(DiscLoader loader) {
		System.out.println("Test");
	}

	@EventHandler
	public void raw(String text) {
		System.out.println(text);
	}

	public void updateViewPanel() {
		DefaultTreeModel model = (DefaultTreeModel) window.panel.root.getModel();
		// load changes to the tree
		model.nodeChanged(window.panel.rootNode);
		window.panel.root.setSelectionPath(window.panel.path);
	}
}
