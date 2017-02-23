/**
 * 
 */
package io.discloader.discloader.common.event;

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
