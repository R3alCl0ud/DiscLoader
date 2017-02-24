package io.discloader.discloader.common.event;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.discovery.ModContainer;
import io.discloader.discloader.common.registry.ModRegistry;

/**
 * @author Perry Berman
 *
 */
public class DLPreInitEvent extends DLEvent {
	
	public final ModContainer activeMod;
	
	public DLPreInitEvent(DiscLoader loader) {
		super(loader);
		this.activeMod = ModRegistry.activeMod;
	}
}
