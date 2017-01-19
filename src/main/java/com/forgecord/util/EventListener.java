package main.java.com.forgecord.util;

import main.java.com.forgecord.client.events.Event;

@FunctionalInterface
public interface EventListener {

	void onEvent(Event event);
}
