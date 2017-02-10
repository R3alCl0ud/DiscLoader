/**
 * 
 */
package io.disc.discloader.objects.loader;

import java.util.ArrayList;

import io.disc.discloader.DiscLoader;
import io.disc.discloader.objects.structures.Command;

/**
 * @author Perry Berman
 *
 */
public class DiscRegistry {

	public final DiscLoader loader;

	public final ArrayList<Command> commands;

	public DiscRegistry(DiscLoader loader) {
		this.loader = loader;

		this.commands = new ArrayList<Command>();
	}

	public void registerCommand(Command command) {
		this.commands.add(command);
	}

}
