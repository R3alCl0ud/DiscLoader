package io.discloader.discloader.test;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.common.event.message.MessageCreateEvent;
import io.discloader.discloader.entity.channel.IGuildTextChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IGuildMember;

public class CommandRunTest extends Command {

	public CommandRunTest() {
		super();
		setUnlocalizedName("runtest");
		setArgsRegex("(\\d+)");
		setUsage("runtest <test number>");
	}

	@Override
	public void execute(MessageCreateEvent e, String[] args) {
		IGuild guild = e.getMessage().getGuild();
		if (guild == null || Main.guild == null || Main.appOwner == null || guild.getID() != Main.guild.getID() || args.length != 1 || e.getMessage().getAuthor().getID() != Main.appOwner.getID()) {
			e.getChannel().sendMessage(getUsage());
			return;
		}
		int testNumber = Integer.parseInt(args[0], 10);
		e.getChannel().sendMessage("Running test #" + testNumber);
		try {
			Main.runTest(testNumber);
			e.getChannel().sendMessage("Test Completed Successfully");
		} catch (Exception ex) {
			e.getChannel().sendMessage("Error occurred whilst executing the test: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	@Override
	public boolean shouldExecute(IGuildMember member, IGuildTextChannel channel) {
		return true;
	}

}
