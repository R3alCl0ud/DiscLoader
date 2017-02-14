/**
 * 
 */
package io.discloader.discloader.events;

import io.discloader.discloader.DiscLoader;

/**
 * @author Perry Berman
 *
 */
public class DiscPreInitEvent extends DiscEvent {
	public DiscPreInitEvent(DiscLoader loader) {
		super(loader);
	}
}
