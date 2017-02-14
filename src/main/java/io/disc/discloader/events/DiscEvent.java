package io.disc.discloader.events;

import io.disc.discloader.DiscLoader;

/**
 * @author Perry Berman
 *
 */
public class DiscEvent {

	public final DiscLoader loader;
	
	public DiscEvent(DiscLoader loader) {
		this.loader = loader;
	}

}
