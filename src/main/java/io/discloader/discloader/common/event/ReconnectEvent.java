package io.discloader.discloader.common.event;

import io.discloader.discloader.common.DiscLoader;

public class ReconnectEvent extends DLEvent {

	private int tries;

	public ReconnectEvent(DiscLoader loader, int t) {
		super(loader);
		tries = t;
	}

	/**
	 * @return the tries
	 */
	public int getTries() {
		return tries;
	}

}
