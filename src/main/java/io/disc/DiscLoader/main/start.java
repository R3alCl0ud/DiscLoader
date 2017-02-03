package io.disc.DiscLoader.main;

import java.awt.Rectangle;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JScrollPane;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.google.gson.Gson;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.events.GuildMemberUpdateEvent;
import io.disc.DiscLoader.events.UserUpdateEvent;
import io.disc.DiscLoader.objects.annotations.eventHandler;
import io.disc.DiscLoader.objects.window.WindowFrame;

/**
 * @author Perry Berman DiscLoader client entry point
 */
public class start {
	public static final Gson gson = new Gson();
	public static WindowFrame window;

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String content = "";
		Object[] lines = Files.readAllLines(Paths.get("./options.json")).toArray();
		for (Object line : lines)
			content += line;
		options options = gson.fromJson(content, options.class);
		DiscLoader loader = new DiscLoader();
		if (options.useWindow == true)
			window = new WindowFrame(loader);
		loader.login(options.auth.token);
	}

	@eventHandler
	public void ready(DiscLoader loader) {
		System.out.println("Test");
		window.panel.load();
		updateViewPanel();
	}

	@eventHandler
	public void raw(String text) {
		System.out.println(text);
	}

	@eventHandler
	public void debug(String debug) {
		System.out.println(debug);
	}

	@eventHandler
	public void UserUpdate(UserUpdateEvent e) {
		
	}

	@eventHandler
	public void PresenceUpdate(GuildMemberUpdateEvent e) {
		window.panel.guilds.guilds.get(e.guild.id).updateMemberNode(e.member);
		updateViewPanel();
	}
	
	public void updateViewPanel() {
		DefaultTreeModel model = (DefaultTreeModel) window.panel.root.getModel();
		// load changes to the tree
		model.nodeChanged(window.panel.rootNode);
		window.panel.root.setSelectionPath(window.panel.path);
	}
}
