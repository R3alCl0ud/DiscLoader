package com.example.commands;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.common.event.message.MessageCreateEvent;

public class ExampleCommand extends Command {

    private final String regex = "(.*)";

    public ExampleCommand() {
        setUnlocalizedName("example").setTextureName("discloader:help").setDescription("example");
        setUsage("example [<args>]").setArgsRegex(regex);
    }

    @Override
    public void execute(MessageCreateEvent e, String[] args) {
        String send = String.join(" ", args);
        e.getChannel().sendMessage(String.format("THis is a test: %S", send));
    }

}