/**
 * 
 */
package io.disc.discloader.events;

import io.disc.discloader.DiscLoader;

/**
 * @author Perry Berman
 *
 */
public class DiscPreInitEvent extends DiscEvent {
	public DiscPreInitEvent(DiscLoader loader) {
		super(loader);
	}
}
