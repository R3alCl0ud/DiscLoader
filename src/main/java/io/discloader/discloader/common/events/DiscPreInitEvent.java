/**
 * 
 */
package io.discloader.discloader.common.events;

import io.discloader.discloader.common.DiscLoader;

/**
 * @author Perry Berman
 *
 */
public class DiscPreInitEvent extends DiscEvent {
	public DiscPreInitEvent(DiscLoader loader) {
		super(loader);
	}
}
