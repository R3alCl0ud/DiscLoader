package io.discloader.discloader.common.event;

import io.discloader.discloader.common.DiscLoader;

/**
 * @author Perry Berman
 *
 */
public class DLEvent {

	public final DiscLoader loader;
	
	public DLEvent(DiscLoader loader) {
		this.loader = loader;
	}

}
