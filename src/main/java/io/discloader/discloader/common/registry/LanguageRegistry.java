package io.discloader.discloader.common.registry;

import java.util.HashMap;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.common.DiscLoader;

/**
 * @author Perry Berman
 *
 */
public class LanguageRegistry {

	public final DiscLoader loader;

	public final HashMap<String, HashMap<Command, String>> localizedNames;

	public LanguageRegistry(DiscLoader loader) {
		this.loader = loader;

		this.localizedNames = new HashMap<String, HashMap<Command, String>>();
	}

	public void setLocalizedName(String locale, Command command, String localizedName) {
		HashMap<Command, String> cmds = this.localizedNames.get(locale);
		if (cmds == null) {
			this.localizedNames.put(locale, cmds = new HashMap<Command, String>());
		}
		cmds.put(command, localizedName);
	}

}
