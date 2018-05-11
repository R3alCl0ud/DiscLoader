package io.discloader.discloader.test;

import java.util.concurrent.ExecutionException;

import io.discloader.discloader.common.event.EventListenerAdapter;
import io.discloader.discloader.common.event.ReadyEvent;
import io.discloader.discloader.common.event.guild.GuildCreateEvent;
import io.discloader.discloader.entity.channel.IGuildTextChannel;
import io.discloader.discloader.network.util.Endpoints;

public class Listener extends EventListenerAdapter {

	/**
	 * @param e
	 */
	@Override
	public void Ready(ReadyEvent e) {
		try {
			Main.appOwner = Main.loader.getSelfUser().getOAuth2Application().get().getOwner();
		} catch (InterruptedException | ExecutionException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void GuildCreate(GuildCreateEvent e) {
		if (e.getGuild().getName().equals(Main.guildName)) {
			Main.guild = e.getGuild();
			IGuildTextChannel channel = Main.guild.getTextChannelByName("general");
			if (channel != null) {
				channel.createInvite().onSuccess(invite -> {
					Main.logger.warning("Invite code for the test guild: " + invite.getCode());
					try {
						if (Main.appOwner != null) {
							Main.appOwner.sendMessage("Testing Area: " + Endpoints.inviteLink(invite.getCode())).get();
						}
					} catch (InterruptedException | ExecutionException e1) {
						e1.printStackTrace();
					}
				}).onException(ex -> {
					ex.printStackTrace();
				}).execute();
			}
		}
	}

}
