package io.discloader.discloader.events;

import io.discloader.discloader.DiscLoader;

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
