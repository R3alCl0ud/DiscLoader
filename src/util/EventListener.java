package util;

import client.events.Event;

@FunctionalInterface
public interface EventListener {

	void onEvent(Event event);
}
