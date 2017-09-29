package io.discloader.discloader.client.command;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import io.discloader.discloader.client.registry.TextureRegistry;
import io.discloader.discloader.client.render.util.IRenderable;
import io.discloader.discloader.client.render.util.Resource;
import io.discloader.discloader.common.event.message.MessageCreateEvent;
import io.discloader.discloader.common.registry.CommandRegistry;
import io.discloader.discloader.entity.channel.IGuildTextChannel;
import io.discloader.discloader.entity.guild.IGuildMember;
import io.discloader.discloader.util.DLNameSpacedMap;

/**
 * @author Perry Berman
 * @since 0.0.1
 */
public class Command {

	protected static DLNameSpacedMap<Command> commands = CommandRegistry.commands;

	public static boolean defaultCommands = true;

	public static void registerCommands() {
		if (defaultCommands) {
			commands.addObject(0, "help", new CommandHelp().setUnlocalizedName("help").setId(0));
			commands.addObject(1, "mods", new CommandMods().setId(1));
			commands.addObject(2, "invite", new CommandInvite().setId(2));
		}
	}

	private String unlocalizedName;

	private String textureName = "discloader:missing-texture";

	private String description = "default description";

	private String fullDescription = null;

	private String argsRegex = "(\\S+)";

	private String usage = null;

	private int id;

	protected IRenderable renderable;

	/**
	 * Creates a new Command
	 */
	public Command() {
		this.renderable = null;
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
	 * executes the command
	 * 
	 * @param e The MessageCreateEvent
	 * @param args The args parsed from {@link #getArgsPattern()}
	 */
	public void execute(MessageCreateEvent e, String[] args) {
		execute(e);
	}

	/**
	 * Get a list of aliases for this command. <b>Never return null!</b>
	 */
	public List<String> getAliases() {
		return new ArrayList<>();
	}

	/**
	 * @return the argsRegex
	 */
	public Pattern getArgsPattern() {
		return Pattern.compile(argsRegex);
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @return the fullDescription
	 */
	public String getFullDescription() {
		return fullDescription;
	}

	public IRenderable getIcon() {
		if (this.renderable == null) {
			this.renderable = TextureRegistry.registerCommandIcon(this);
		}
		return this.renderable;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	public Resource getResourceLocation() {
		return new Resource("DiscLoader", "texture/commands/missing-texture.png");
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
		return CommandHandler.prefix + this.usage;
	}

	/**
	 * @param argsRegex the argsRegex to set
	 */
	public void setArgsRegex(String argsRegex) {
		this.argsRegex = argsRegex;
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
	 * @param fullDescription the fullDescription to set
	 */
	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
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

	public boolean shouldExecute(IGuildMember member, IGuildTextChannel channel) {
		return true;
	}

}
