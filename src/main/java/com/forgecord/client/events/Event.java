package main.java.com.forgecord.client.events;

import org.json.JSONArray;

public class Event {
	
	public String message;
	public JSONArray data;
	
	public Event(String message) {
		this.message = message;
	}
}
