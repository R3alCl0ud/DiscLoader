package io.discloader.discloader.client.command;

import java.time.format.DateTimeFormatter;

import io.discloader.discloader.common.event.message.MessageCreateEvent;
import io.discloader.discloader.core.entity.RichEmbed;
import io.discloader.discloader.entity.channel.ITextChannel;
import io.discloader.discloader.entity.guild.IGuild;

/**
 * @author Perry Berman
 *
 */
public class CommandInfo extends Command {
	
	public CommandInfo() {
		setUnlocalizedName("info");
	}
	
	public void execute(MessageCreateEvent event, String[] args) {
		IGuild guild = event.getMessage().getGuild();
		if (guild == null)
			return;
		ITextChannel channel = event.getChannel();
		RichEmbed embed = new RichEmbed(guild.getName()).setColor(0x427df4);
		embed.setDescription(guild.createdAt().format(DateTimeFormatter.ofPattern("d MMM uuuu hh:mm")));
		embed.addField("Owner", guild.getOwner().toString(), true);
		// embed.setTimestamp(OffsetDateTime.now());
		embed.setFooter(String.format("Guild ID: %d", guild.getID()));
		channel.sendEmbed(embed);
		
	}
	
}
