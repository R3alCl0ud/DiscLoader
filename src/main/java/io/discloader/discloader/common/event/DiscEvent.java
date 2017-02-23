package io.discloader.discloader.common.event;

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
