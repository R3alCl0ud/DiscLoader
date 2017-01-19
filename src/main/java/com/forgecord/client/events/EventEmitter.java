package main.java.com.forgecord.client.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import org.json.JSONArray;
import org.json.JSONObject;

@SuppressWarnings("rawtypes")
public class EventEmitter {
	
	public Listeners onHandlers;
	public Listeners onceHandlers;
	
	public EventEmitter() {
		this.onHandlers = new Listeners();
		this.onceHandlers = new Listeners();
	}
	/**
	 * Adds an event handler to the {@link EventEmitter}
	 * @param event The event to listen for
	 * @param handler The function that handles the event
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public EventEmitter on(String event, Consumer handler) {
		this.onHandlers.add(event, handler);
		return this;
	}
	
	/**
	 * Adds an event handler that will remove itself from the {@link EventEmitter}'s listeners after being called once
	 * @param event The event to listen for
	 * @param handler The function that handles the event
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public EventEmitter once(String event, Consumer handler) {
		this.onceHandlers.add(event, handler);
		return this;
	}
	
	/**
	 * Makes the {@link EventEmitter} call all on and once handlers for the emitted event
	 * @param event The emitted event
	 * @param data The data to pass to the handlers
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public EventEmitter emit(String event, Object data) {
		this.onceHandlers.execute(event, true, data);
		this.onHandlers.execute(event, false, data);
		return this;
	}
	
	class Listeners {
		private HashMap<String, HashMap> events = new HashMap<String, HashMap>();
		
		public Listeners() {
			
		}
		
		public void add(String event, Consumer handler) {
			if (!events.containsKey(event)) events.put(event, new HashMap<String, Consumer>());
			events.get(event).put(handler.toString(), handler);
		}
		
		public void execute(String event, boolean remove, Object data) {
			if (!events.containsKey(event)) return;
			events.get(event).forEach((key, action) -> {
				Consumer handler = (Consumer) action;
				handler.accept(data);
				if (remove) events.get(event).remove(key);
			});
		}
	}
}
