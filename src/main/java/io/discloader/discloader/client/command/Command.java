package io.discloader.discloader.client.command;

import io.discloader.discloader.client.registry.TextureRegistry;
import io.discloader.discloader.client.render.util.IIcon;
import io.discloader.discloader.common.event.MessageCreateEvent;
import io.discloader.discloader.common.registry.CommandRegistry;
import io.discloader.discloader.util.DLNameSpacedMap;

/**
 * 
 * 
 * @author Perry Berman
 * @since 0.0.1
 */
public class Command {

    protected static DLNameSpacedMap<Command> commands = CommandRegistry.commands;

    public static void registerCommands() {
        commands.addObject(0, "help", new CommandHelp().setUnlocalizedName("help").setId(0));
        commands.addObject(1, "mods", new CommandMods().setId(1));
        commands.addObject(2, "invite", new CommandInvite().setId(2));
    }

    private String unlocalizedName;

    private String textureName = "discloader:missing-texture";

    private String description = "default description";

    private String usage = null;

    private int id;

    protected IIcon icon;

    /**
     * Creates a new Command
     */
    public Command() {
        this.icon = null;
    }

    /**
     * executes the command
     * 
     * @param e The MessageCreateEvent
     */
    public void execute(MessageCreateEvent e) {
        return;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    public IIcon getIcon() {
        if (this.icon == null) {
            this.icon = TextureRegistry.registerCommandIcon(this);
        }
        return this.icon;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the textureName
     */
    public String getTextureName() {
        return textureName;
    }

    /**
     * @return the unlocalizedName
     */
    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    /**
     * @return the usage
     */
    public String getUsage() {
        if (this.usage == null) {
            this.usage = this.getUnlocalizedName();
        }
        return this.usage;
    }

    /**
     * @param description the description to set
     * @return this.
     */
    public Command setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * @param id the id to set
     * @return this.
     */
    public Command setId(int id) {
        this.id = id;
        return this;
    }

    /**
     * Sets the name of the texture for this command
     * 
     * @param textureName MODID:icons.commands.unlocalizedName
     * @return this.
     */
    public Command setTextureName(String textureName) {
        this.textureName = textureName;
        return this;
    }

    /**
     * @param unlocalizedName The new unlocalizedName for the command
     * @return this
     */
    public Command setUnlocalizedName(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;
        return this;
    }

    /**
     * @param usage the usage to set
     * @return this.
     */
    public Command setUsage(String usage) {
        this.usage = usage;
        return this;
    }

}
