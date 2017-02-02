package io.disc.DiscLoader;

import io.disc.DiscLoader.events.GuildMemberUpdateEvent;
import io.disc.DiscLoader.events.UserUpdateEvent;
import io.disc.DiscLoader.objects.annotations.Mod;
import io.disc.DiscLoader.objects.annotations.eventHandler;
import io.disc.DiscLoader.objects.window.WindowFrame;

@Mod(modid = "Test Client", version = "0.0.1_a", desc = "A test client for DiscLoader API")
public class testClient {
	public static WindowFrame frame;

	public static void main(String... args) {
		DiscLoader client = new DiscLoader();
		frame = new WindowFrame(client);
		client.login("MjcxNjYwMzYxMTMwODM1OTcx.C2gSGw.pCPSCH3b_lIvsrTjMl8QmZ_iPjs");

	}

	@eventHandler
	public void raw(String raw) {
//		frame.tree.ready();
//		System.out.println(raw);
	}

	@eventHandler
	public void debug(String debug) {
		System.out.println(debug);
	}

	@eventHandler
	public void ready(DiscLoader loader) {
		System.out.println("Hey looks like we're ready");
		frame.panel.load();
		System.out.println(loader.channels.get("190559195031011330").name);
	}
	
	@eventHandler
	public void UserUpdate(UserUpdateEvent e) {
//		frame.tree.users.updateUserNode(e.user);
//		System.out.printf("%s, %s\n",e.user.username, e.oldUser.username);
	}
	
	@eventHandler
	public void PresenceUpdate(GuildMemberUpdateEvent e) {
//		frame.tree.guilds.guilds.get(e.guild.id).updateMemberNode(e.member);
	}
}
