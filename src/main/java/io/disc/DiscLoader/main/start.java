package io.disc.DiscLoader.main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.tree.DefaultTreeModel;

import com.google.gson.Gson;

import io.disc.DiscLoader.DiscLoader;
import io.disc.DiscLoader.events.GuildMemberUpdateEvent;
import io.disc.DiscLoader.events.UserUpdateEvent;
import io.disc.DiscLoader.objects.annotations.eventHandler;
import io.disc.DiscLoader.objects.structures.Message;
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
	public static void main(String... args) throws IOException {
		String content = "";
		Object[] lines = Files.readAllLines(Paths.get("./options.json")).toArray();
		for (Object line : lines)
			content += line;
		options options = gson.fromJson(content, options.class);
		DiscLoader loader = new DiscLoader();
		if (args.length == 0 || !args[0].equals("nogui"))
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
	public void MessageCreate(Message message) {
		System.out.println(message.channel.id);
		if (!message.loader.user.bot && message.author.id != message.loader.user.id) return;
		
		if (message.content.equalsIgnoreCase("//test")) {
			message.channel.sendMessage("Hello there!\nGuild: " + (message.guild != null));
		} else if (message.content.startsWith("//nick") && message.guild != null && message.author.id.equals("104063667351322624")) {
			String nick = message.content.split(" ")[1];
			message.guild.members.get(message.loader.user.id).setNick(nick);
		}
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
//		window.panel.guilds.guilds.get(e.guild.id).updateMemberNode(e.member);
//		updateViewPanel();
	}
	
	public void updateViewPanel() {
		DefaultTreeModel model = (DefaultTreeModel) window.panel.root.getModel();
		// load changes to the tree
		model.nodeChanged(window.panel.rootNode);
		window.panel.root.setSelectionPath(window.panel.path);
	}
}
