package io.discloader.discloader.common.events;

import io.discloader.discloader.common.DiscLoader;

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
